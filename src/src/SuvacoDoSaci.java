package Suvaqueira;
import robocode.*;
import java.awt.Color;

/**
 * Curupira - Um robô com estratégia diferente (Strafer/Zigue-Zague)
 * Ao invés de fugir para as paredes, ele orbita o inimigo.
 */
public class Curupira extends AdvancedRobot {

    // Controla se o robô está a andar para a frente (1) ou para trás (-1)
    private int direcao = 1;

    public void run() {
        // Cores inspiradas no Curupira (folhas e fogo)
        setColors(Color.green, Color.black, Color.red);

        // Separa as partes do robô para se moverem de forma independente
        setAdjustRadarForRobotTurn(true);
        setAdjustGunForRobotTurn(true);
        setAdjustRadarForGunTurn(true);

        // Fica a girar o radar loucamente até achar alguém
        setTurnRadarRightRadians(Double.POSITIVE_INFINITY);
    }

    public void onScannedRobot(ScannedRobotEvent e) {
        // 1. Radar Lock Simples e Rápido
        double radarTurn = robocode.util.Utils.normalRelativeAngle(getHeadingRadians() + e.getBearingRadians() - getRadarHeadingRadians());
        setTurnRadarRightRadians(radarTurn * 1.9); // O 1.9 cria um arco de varredura largo o suficiente para não perder o alvo

        // 2. Movimento Circular (Strafing)
        // Fica sempre a 90 graus (perpendicular) ao inimigo para ser difícil de acertar
        // O "- (15 * direcao)" faz com que ele se aproxime aos pouquinhos em espiral
        setTurnRight(e.getBearing() + 90 - (15 * direcao));
        setAhead(150 * direcao);

        // 3. Mira Direta Simples (Diferente da mira preditiva)
        double gunTurn = robocode.util.Utils.normalRelativeAngle(getHeadingRadians() + e.getBearingRadians() - getGunHeadingRadians());
        setTurnGunRightRadians(gunTurn);

        // 4. Força do tiro baseada na distância
        double forcaTiro = Math.min(400 / e.getDistance(), 3);
        
        // Atira se a arma estiver pronta e apontada
        if (getGunHeat() == 0 && Math.abs(gunTurn) < 0.1) {
            setFire(forcaTiro);
        }
    }

    /**
     * Se bater numa parede, inverte a marcha imediatamente
     * para não ficar preso.
     */
    public void onHitWall(HitWallEvent e) {
        direcao = -direcao;
    }

    /**
     * Se esbarrar noutro robô, muda de direção para fugir
     */
    public void onHitRobot(HitRobotEvent e) {
        direcao = -direcao;
    }
}