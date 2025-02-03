//this code was improvised from FTC team 18915, Artemis
//their code can be found on github at https://github.com/artemis18715/New-Programming-Tutorial-23-24

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.util.ElapsedTime;

public class PIDControl {
    private static double integralSum = 0;
    private static final double Kp = PIDConstants.Kp;
    private static final double Ki = PIDConstants.Ki;
    private static final double Kd = PIDConstants.Kd;

    private static final ElapsedTime timer = new ElapsedTime();
    private static double lastError = 0;

    public static double turnToPosition(double reference, double state) {
        double error = angleWrap(reference - state);
        //telemetry.addData("Error: ", error);
        integralSum += error * timer.seconds();
        double derivative = (error - lastError) / (timer.seconds());
        lastError = error;
        timer.reset();
        return (error * Kp) + (derivative * Kd) + (integralSum * Ki);
    }

    private static double angleWrap(double degrees){
        while(degrees > 180){
            degrees -= 360;
        }
        while(degrees < -180){
            degrees += 360;
        }
        return degrees;
    }
}
