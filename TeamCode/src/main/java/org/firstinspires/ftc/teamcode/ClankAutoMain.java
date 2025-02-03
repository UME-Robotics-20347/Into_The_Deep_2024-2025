package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

@Autonomous(name="Clank Auto Main", group="Autonomous Bread")
public class ClankAutoMain extends LinearOpMode {

    static final double countsPerMotorRev = 420;
    static final double wheelCircumferenceInches = 3.625 * Math.PI;//prevoiusly was 3.5
    static final double countsPerInch = countsPerMotorRev / wheelCircumferenceInches;
    static final double countsPerDegree = (countsPerInch * (14.31 * Math.PI) / 360) * (4.1/3.0); // 1.5 is a magic number, idk why it workso

    //IMU stuff
    private BNO055IMU imu;

    @Override
    public void runOpMode() {
        //1. init
        Hardware.init(hardwareMap);

        Hardware.armMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        //IMU stuff
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.mode = BNO055IMU.SensorMode.IMU;
        parameters.angleUnit = BNO055IMU.AngleUnit.RADIANS;
        imu.initialize(parameters);

        resetMotors();
        waitForStart();

        //↶, ↷, ↑, ↓, °
        // As far as our robot knows, 360 = 90° (turning wise with encoders)
        //1. ↑24"
        encoderDrive(-1, -30, 10);
        //turn wrist
        Hardware.wrist.setPosition(0);
        sleep(1000);
        //move arm
        //armRunE(1, -1600, 2100);
        armRun(-0.6, 1000);
        //turn wrist
        Hardware.wrist.setPosition(0.5);
        //drive back a little
        encoderDrive(-1, 4, 10);
        //drive back to the wall
        encoderDrive(-1, 28, 10);
        //move arm
        //armRunE(1, -1600, 2100);
        armRun(-0.25, 1000);
        //turn wrist
        Hardware.wrist.setPosition(0);
        //turn right 90 degrees
        IMUTurn(89, 100);
        //encoderTurn(-1, -400, 10);
        //intake starts
        Hardware.inTake.setPower(1);
        //drive to park
        encoderDrive(-0.75, -28, 10);
        //intake stop
        sleep(500);
        Hardware.inTake.setPower(0);
        //pull arm back
        //armRunE(1, 3000, 2000);
        armRun(0.75, 1000);

        //drive back to the rungs
        encoderDrive(-1, 30,10);
        //turn left 90 degrees
        IMUTurn(0, 100);
        //encoderTurn(-1, 400, 10);
        //drive up to rungs
        encoderDrive(-1, -24, 10);

        //move arm forward
        //armRunE(1, -1000, 2000);
        armRun(-0.5, 1000);
        //turn wrist
        Hardware.wrist.setPosition(0.5);
        //move arm forward
        //armRunE(1, -1000, 2000);
        armRun(-0.5, 500);
        //drive back a little
        sleep(500);
        encoderDrive(-1, 4, 10);
        //spit out
        /*Hardware.inTake.setPower(1);
        sleep(1000);
        Hardware.inTake.setPower(0);*/
        //drive back to the wall
        encoderDrive(-1, 20, 10);
        //turn wrist
        Hardware.wrist.setPosition(0);
        //turn right 90 degrees
        IMUTurn(90, 100);
        //encoderTurn(-1, -400, 10);
        //drive to park
        encoderDrive(-1, -40, 10);
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

    //E for encoder
    public void armRunE(double speed, double position, int msToWaitAfter) {
        //the arms counts per revolution is 2800
        Hardware.armMotor.setTargetPosition((int) (position));
        Hardware.armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        resetRuntime();
        Hardware.armMotor.setPower(speed);

        while (busy()) {
            telemetry.addLine("Running..");
            telemetry.update();
        }
        resetMotors();
        sleep(msToWaitAfter);
        Hardware.armMotor.setPower(0);
    }

    public void armRun(double speed, int msDuration) {
        Hardware.armMotor.setPower(speed);
        sleep(msDuration);
        while (busy()) {
            telemetry.addLine("Running..");
            telemetry.update();
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

    public void IMUTurn(double referenceAngle, int msToWaitAfter) {
        double range = 3;
        boolean run = true;// needed to make sure it runs correctly,
        double power;
        /*
        Hardware.backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        Hardware.backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        Hardware.frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        Hardware.frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        //*/

        while(opModeIsActive() && run) {
            telemetry.addData("Target IMU Angle", referenceAngle);
            telemetry.addData("Current IMU Angle", -imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle);
            if (((-imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle) >= referenceAngle - range) && ((-imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle) <= referenceAngle + range)) {
                power = 0;// null obj ref otherwise
                run = false;
            } else {
                power = PIDControl.turnToPosition(-referenceAngle, imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle);
                power *= 2;
            }

            Hardware.frontLeft.setPower(power);
            Hardware.backLeft.setPower(power);
            Hardware.frontRight.setPower(power);
            Hardware.backRight.setPower(power);

            telemetry.addLine("Running..");
            telemetry.update();
        }
        /*
        Hardware.backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Hardware.backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Hardware.frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        Hardware.frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //*/
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