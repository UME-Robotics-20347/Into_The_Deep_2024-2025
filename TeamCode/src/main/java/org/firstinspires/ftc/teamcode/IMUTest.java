//this code was improvised from FTC team 18915, Artemis
//their code can be found on github at https://github.com/artemis18715/New-Programming-Tutorial-23-24

package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;

@Autonomous(name = "IMU Test", group="Autonomous Bread")
public class IMUTest extends LinearOpMode {
    double integralSum = 0;
    double Kp = PIDConstants.Kp;
    double Ki = PIDConstants.Ki;
    double Kd = PIDConstants.Kd;

    ElapsedTime timer = new ElapsedTime();
    private double lastError = 0;
    private BNO055IMU imu;

    @Override
    public void runOpMode() throws InterruptedException {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        Hardware.init(this.hardwareMap);

        Hardware.backLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        Hardware.backRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        Hardware.frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        Hardware.frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        Hardware.armMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        imu = hardwareMap.get(BNO055IMU.class, "imu");
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.mode = BNO055IMU.SensorMode.IMU;
        parameters.angleUnit = BNO055IMU.AngleUnit.RADIANS;
        imu.initialize(parameters);

        double refrenceAngle = 90;
        waitForStart();

        while(opModeIsActive()) {
            double range = 3;

            telemetry.addData("Target IMU Angle", refrenceAngle);
            telemetry.addData("Current IMU Angle", -imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle);
            double power = PIDControl.turnToPosition(-refrenceAngle, imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle);
            if (((-imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle) >= refrenceAngle - range) && ((-imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES).firstAngle) <= refrenceAngle + range)) {
                requestOpModeStop();
                power = 0;
            }

            Hardware.frontLeft.setPower(power);
            Hardware.backLeft.setPower(power);
            Hardware.frontRight.setPower(power);
            Hardware.backRight.setPower(power);
            telemetry.update();
        }
    }
}