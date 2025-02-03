package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name="Clank Auto Alt", group="Autonomous Bread")
public class ClankAutoAlt extends LinearOpMode {

    static final double countsPerMotorRev = 140;
    static final double wheelCircumferenceInches = 3.5 * Math.PI;
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
        //linearSlideTo(0);

        //↶, ↷, ↑, ↓, °

        //1. ↑48"
        encoderDrive(-1, -78, 10);
        //turn wrist
        Hardware.wrist.setPosition(0);
        //Hardware.wrist.setPower(-0.1);
        sleep(1000);
        //move arm
        Hardware.armMotor.setPower(-0.5);
        sleep(1300);
        Hardware.armMotor.setPower(0);
        //drive back a little
        encoderDrive(-2, 20, 10);
        //spit out
        Hardware.inTake.setPower(-1);
        sleep(1000);
        Hardware.inTake.setPower(0);
        //drive back slightly
        encoderDrive(-1, 10, 10);
        //move arm back
        //Hardware.linearSlide.setPower(0.25);
        //sleep(2000);
        //Hardware.linearSlide.setPower(0);
        //drive back to the wall
        encoderDrive(-1, 50, 10);
        //turn 90 degrees
        //encoderTurn(-0.5, -240, 10);
        //drive to park
        //encoderDrive(-1, -72, 10);

        //4. ↑48"
        //encoderDrive(-0.5, -48, 10);
        //5. slide↓=0, drop

        //linearSlideTo(0);
        //Hardware.claw.setPower(0.5);
        //sleep(1000);
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
        Hardware.backRight.setTargetPosition(-(int)Math.round(inches * countsPerInch));
        Hardware.frontLeft.setTargetPosition((int)Math.round(inches * countsPerInch));
        Hardware.frontRight.setTargetPosition(-(int)Math.round(inches * countsPerInch));

        Hardware.backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Hardware.backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Hardware.frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Hardware.frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        //Start motion.
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

    public void linearSlideTo(int position) {
        Hardware.armMotor.setTargetPosition(position);
        Hardware.armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Hardware.armMotor.setPower(0.5);
        while (Hardware.armMotor.isBusy()) {
            telemetry.addLine("Raising linear slide..");
        }
        Hardware.armMotor.setPower(0);
    }
    public void encoderTurn(double speed, double turnDegrees, int msToWaitAfter) {
        resetMotors();
        Hardware.backLeft.setTargetPosition((int) (turnDegrees * countsPerDegree));
        Hardware.backRight.setTargetPosition((int) (turnDegrees * countsPerDegree));
        Hardware.frontLeft.setTargetPosition((int) (turnDegrees * countsPerDegree));
        Hardware.frontRight.setTargetPosition((int) (turnDegrees * countsPerDegree));

        //Turn On RUN_TO_POSITION
        Hardware.backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Hardware.backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Hardware.frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Hardware.frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        //reset the timeout time and start motion.
        resetRuntime();
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