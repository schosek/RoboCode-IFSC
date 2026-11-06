package SuvacoDoSaci;

import robocode.*;
import robocode.util.Utils;
import java.awt.Color;

public class SuvacoDoSaci extends AdvancedRobot {

    private int patrolDir = 1;
    private long lastSeen = -100;

    public void run() {
        setColors(Color.blue, Color.white, Color.red);

        setAdjustGunForRobotTurn(true);
        setAdjustRadarForGunTurn(true);

        while (true) {
            boolean procurando = getTime() - lastSeen > 8;

            if (procurando
                    && getDistanceRemaining() == 0
                    && getGunTurnRemaining() == 0
                    && getRadarTurnRemaining() == 0) {
                patrolDir = -patrolDir;
                setAhead(100 * patrolDir);
                setTurnGunRight(360);
                setTurnRadarRight(360); 
            }

            execute();
        }
    }

    public void onScannedRobot(ScannedRobotEvent e) {
        lastSeen = getTime();

        double absBearing = getHeading() + e.getBearing();

        setTurnRadarRight(Utils.normalRelativeAngleDegrees(absBearing - getRadarHeading()) * 2);

        setTurnGunRight(Utils.normalRelativeAngleDegrees(absBearing - getGunHeading()));

        setTurnRight(e.getBearing());
        if (e.getDistance() > 120) {
            setAhead(e.getDistance() - 100);
        } else {
            setAhead(0);
        }

        if (getGunHeat() == 0 && Math.abs(getGunTurnRemaining()) < 10) {
            if (e.getDistance() < 150) {
                setFire(3);
            } else {
                setFire(1);
            }
        }
    }

    public void onHitByBullet(HitByBulletEvent e) {
        setTurnLeft(90 - e.getBearing());
        setAhead(50);
    }

    public void onHitWall(HitWallEvent e) {
        setBack(50);
        setTurnRight(90);
    }

    public void onHitRobot(HitRobotEvent e) {
        double absBearing = getHeading() + e.getBearing();
        setTurnGunRight(Utils.normalRelativeAngleDegrees(absBearing - getGunHeading()));
        if (getGunHeat() == 0 && Math.abs(getGunTurnRemaining()) < 10) {
            setFire(3);
        }
        setBack(50);
    }
}
