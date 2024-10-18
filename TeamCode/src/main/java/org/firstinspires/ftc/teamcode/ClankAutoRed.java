package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name="Clank Autonomous (Red)", group="Autonomous Bread")
public class ClankAutoRed extends LinearOpMode {

    static final double countsPerMotorRev = 140;
    static final double wheelCircumferenceInches = 3.78 * Math.PI;
    static final double countsPerInch = countsPerMotorRev / wheelCircumferenceInches;
    static final double countsPerDegree = (countsPerInch * (14.31 * Math.PI) / 360) * (4.1/3.0); // 1.5 is a magic number, idk why it workso

    @Override
    public void runOpMode() {
        //1. init
        Hardware.init(this.hardwareMap);
        resetMotors();
        //Hardware.claw.setPower(-0.3);

        waitForStart();

        //Hardware.inTake.setPower(0.1);
        sleep(500);
        linearSlideTo(0);

        //↶, ↷, ↑, ↓, °

        //2. ↑1"
        encoderDrive(-0.5, -1, 10);
        //3. ↷90°
        encoderTurn(-0.5, -90, 10);
        //4. ↑48"
        encoderDrive(-0.5, -48, 10);
        //5. slide↓=0, drop

        linearSlideTo(0);
        //Hardware.claw.setPower(0.5);
        sleep(1000);
/*
        //6. slide↑=72
        linearSlideTo(-72);
        //7. ↓48"
        encoderDrive(0.5, 48, 10);
        //8. ↶90°
        encoderTurn(0.5, 90, 10);
        //9. ↑21"
        encoderDrive(-0.5,-21,10);
        //10. slide↓=0, grab

        linearSlideTo(0);
        Hardware.claw.setPower(-0.3);

        //11. slide↑=72
        linearSlideTo(-72);
        //12. ↓21"
        encoderDrive(0.5, 21, 10);
        //13. ↷90°
        encoderTurn(-0.5, -90, 10);
        //14. ↑48"
        encoderDrive(-0.5, -48, 10);
        //15. slide↓=0, drop
        linearSlideTo(0);
        Hardware.claw.setPower(0.5);
        sleep(1000);
*/
    }

    public void encoderDrive(double speed, double inches, int msToWaitAfter) {
        resetMotors();

        Hardware.backLeft.setTargetPosition((int)Math.round(inches * countsPerInch));
        Hardware.backRight.setTargetPosition((int)Math.round(inches * countsPerInch));
        Hardware.frontLeft.setTargetPosition((int)Math.round(inches * countsPerInch));
        Hardware.frontRight.setTargetPosition((int)Math.round(inches * countsPerInch));

        Hardware.backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Hardware.backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Hardware.frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Hardware.frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        //Start motion.
        Hardware.backLeft.setPower(speed);
        Hardware.backRight.setPower(speed);
        Hardware.frontLeft.setPower(speed);
        Hardware.frontRight.setPower(speed);

        while (busy()) {
            telemetry.addLine("Running..");
            telemetry.update();
        }

        resetMotors();
        sleep(msToWaitAfter);
    }

    public void linearSlideTo(int position) {
        Hardware.linearSlide.setTargetPosition(position);
        Hardware.linearSlide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Hardware.linearSlide.setPower(0.5);
        while (Hardware.linearSlide.isBusy()) {
            telemetry.addLine("Raising linear slide..");
        }
        Hardware.linearSlide.setPower(0);
    }
    public void encoderTurn(double speed, double turnDegrees, int msToWaitAfter) {
        resetMotors();
        Hardware.backLeft.setTargetPosition((int) (turnDegrees * countsPerDegree));
        Hardware.backRight.setTargetPosition((int) (-turnDegrees * countsPerDegree));
        Hardware.frontLeft.setTargetPosition((int) (turnDegrees * countsPerDegree));
        Hardware.frontRight.setTargetPosition((int) (-turnDegrees * countsPerDegree));

        //Turn On RUN_TO_POSITION
        Hardware.backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Hardware.backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Hardware.frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Hardware.frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        //reset the timeout time and start motion.
        resetRuntime();
        Hardware.backLeft.setPower(speed);
        Hardware.backRight.setPower(-speed);
        Hardware.frontLeft.setPower(speed);
        Hardware.frontRight.setPower(-speed);

        while (busy()) {
            telemetry.addLine("Running..");
            telemetry.update();
        }

        resetMotors();
        sleep(msToWaitAfter);
    }

    public void resetMotors() {
        Hardware.backLeft.setPower(0);
        Hardware.backRight.setPower(0);
        Hardware.frontLeft.setPower(0);
        Hardware.frontRight.setPower(0);

        Hardware.backLeft.setTargetPosition(Hardware.backLeft.getCurrentPosition());
        Hardware.backRight.setTargetPosition(Hardware.backRight.getCurrentPosition());
        Hardware.frontLeft.setTargetPosition(Hardware.frontLeft.getCurrentPosition());
        Hardware.frontRight.setTargetPosition(Hardware.frontLeft.getCurrentPosition());

        Hardware.backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Hardware.backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Hardware.frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Hardware.frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        Hardware.backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Hardware.backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Hardware.frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Hardware.frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
    private boolean busy() {
        return opModeIsActive() && (Hardware.backLeft.isBusy() && Hardware.backRight.isBusy() && Hardware.frontLeft.isBusy() && Hardware.frontRight.isBusy());
    }
}