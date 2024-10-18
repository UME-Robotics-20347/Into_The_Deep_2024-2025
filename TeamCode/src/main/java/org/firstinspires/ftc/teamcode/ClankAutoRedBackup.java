package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name="Clank Autonomous (Red) backup", group="Autonomous Bread")
public class ClankAutoRedBackup extends LinearOpMode {

    static final double countsPerMotorRev = 28 * 5; //5 is the gearbox ratio.
    static final double wheelCircumferenceInches = 3.78 * Math.PI;
    static final double countsPerInch = countsPerMotorRev / wheelCircumferenceInches;

    static final double countsPerDegree = (countsPerInch * (14.31 * Math.PI) / 360) * (4.0/3.0);

    @Override
    public void runOpMode() {
        Hardware.init(this.hardwareMap);

        resetMotors();

        waitForStart();
        encoderTurn(-0.5, -90, 10);
        encoderDrive(-0.5, -48, 100);
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
            telemetry.update();
        }

        resetMotors();
        sleep(msToWaitAfter);
    }

    //positive: counter-clockwise, negative: clockwise
    public void encoderTurn(double speed, double degrees, int msToWaitAfter) {
        resetMotors();
        Hardware.backLeft.setTargetPosition((int) Math.round(degrees * countsPerDegree));
        Hardware.backRight.setTargetPosition((int) Math.round(-degrees * countsPerDegree));
        Hardware.frontLeft.setTargetPosition((int) Math.round(degrees * countsPerDegree));
        Hardware.frontRight.setTargetPosition((int) Math.round(-degrees * countsPerDegree));

        //Turn On RUN_TO_POSITION
        Hardware.backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Hardware.backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Hardware.frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        Hardware.frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        //reset the timeout time and start motion.

        Hardware.backLeft.setPower(speed);
        Hardware.backRight.setPower(-speed);
        Hardware.frontLeft.setPower(speed);
        Hardware.frontRight.setPower(-speed);

        while (busy()) {
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