package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

@Autonomous(name="Clank Test Auto", group="Autonomous Bread")
public class ClankTestAuto extends LinearOpMode {

    static final double countsPerMotorRev = 140;
    static final double wheelCircumferenceInches = 3.5 * Math.PI;
    static final double countsPerInch = countsPerMotorRev / wheelCircumferenceInches;
    static final double countsPerDegree = (countsPerInch * (14.31 * Math.PI) / 360) * (4.1/3.0); // 1.5 is a magic number, idk why it workso

    //IMU stuff
    double integralSum = 0;
    final double Kp = PIDConstants.Kp;
    final double Ki = PIDConstants.Ki;
    final double Kd = PIDConstants.Kd;
    ElapsedTime timer = new ElapsedTime();
    private double lastError = 0;
    private BNO055IMU imu;

    @Override
    public void runOpMode() {
        Hardware.init(this.hardwareMap);

        //IMU stuff
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.mode = BNO055IMU.SensorMode.IMU;
        parameters.angleUnit = BNO055IMU.AngleUnit.RADIANS;
        imu.initialize(parameters);

        resetMotors();
        waitForStart();

        IMUTurn(90, 1000);

        IMUTurn(0, 1000);

        IMUTurn(180, 1000);
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

    public void armRun(double speed, double position, int msToWaitAfter) {
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

    public void IMUTurn(double refrenceAngle, int msToWaitAfter) {
        double range = 3;

        while(range == range) {
            telemetry.addData("Target IMU Angle", refrenceAngle);
            telemetry.addData("Current IMU Angle", -imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle);
            double power = PIDControl.turnToPosition(-refrenceAngle, imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle);
            if (((-imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle) >= refrenceAngle - range) && ((-imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle) <= refrenceAngle + range)) {
                //requestOpModeStop();
                power = 0;
                break;
            }

            Hardware.frontLeft.setPower(power);
            Hardware.backLeft.setPower(power);
            Hardware.frontRight.setPower(power);
            Hardware.backRight.setPower(power);

            telemetry.addLine("Running..");
            telemetry.update();
        }
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