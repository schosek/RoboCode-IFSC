package Suvaqueira;
import robocode.*;
import java.awt.Color;


// API help : https://robocode.sourceforge.io/docs/robocode/robocode/Robot.html

    /**
     * SuvacoDoSaci - a robot by Felipe, Guilherme, Bernado Z
     */
    public class SuvacoDoSaci extends AdvancedRobot
    {
        /**
         * run: SuvacoDoSaci's default behavior
         */
        public void run() {
		 setColors(Color.red,Color.blue,Color.red); // body,gun,radar

//-----------------------------------COLETANDO OS DADOS DA ARENA----------------------------------------------------
            // Pegando a posição do robo
			double x = getX();
 			double y = getY();
			double largura = getBattleFieldWidth();
			double altura = getBattleFieldHeight();
			//Cauculando as distancias até a parede
			double disEsquerda = (getBattleFieldWidth()) - x;
			double disCima = (getBattleFieldHeight()) - y;
			

//----------------------------------MAPA DA ARENA-------------------------------------------------------------------

 // +-----------------------------+-----------------------------+
 // | CANTO SUPERIOR ESQUERDO     | CANTO SUPERIOR DIREITO      |
 // |                             |                             |
 // | x < (largura / 2)           | x > (largura / 2)           |
 // | y > (altura / 2)            | y > (altura / 2)            |
 // |                             |                             |
 // +-----------------------------+-----------------------------+ 
 // | CANTO INFERIOR ESQUERDO     | CANTO INFERIOR DIREITO      |
//  |                             |                             |
 // | x < (largura / 2)           | x > (largura / 2)           |
//  | y < (altura / 2)            | y < (altura / 2)            |
//  |                             |                             |
//  +-----------------------------+-----------------------------+

//---------------------------MOVIMENTO INICAL ----------------------------------------------------------------------
	//Pegando os dados para calcualar os angulos das pontas
			 double destinoX = 0;
			 double destinoY = 0; 		
			 double anguloAlinhamento = 0;	

            if (x < largura/2 && y > altura/2) {
			    destinoX = 60;
			    destinoY = altura - 60;
			    anguloAlinhamento = 90;
			} else if (x >= largura/2 && y >= altura/2) {
			    destinoX = largura - 60;
			    destinoY = altura - 60;
			    anguloAlinhamento = 180;
			} else if (x <= largura/2 && y <= altura/2) {
			    destinoX = 60;
			    destinoY = 60;
			    anguloAlinhamento = 0;
			} else {
			    destinoX = largura - 60;
			    destinoY = 60;
			    anguloAlinhamento = 270;
			}
	// Calculando os valores para achar o caminho mais rapido até a parede		
			double dx = destinoX - x;
			double dy = destinoY - y;
			double menorDis = Math.hypot(dx, dy);
			double melhorAng = Math.toDegrees(Math.atan2(dx, dy));
			double giroNecessario = robocode.util.Utils.normalRelativeAngleDegrees(melhorAng - getHeading());
			
			
			turnRight(giroNecessario);
			ahead(menorDis);

			double giroFinal = robocode.util.Utils.normalRelativeAngleDegrees(anguloAlinhamento - getHeading());
			turnRight(giroFinal);
            // Robot main loop
            while(true) {
            int direcao = (int) Math.round(getHeading()); 
		    if (direcao == 0 || direcao == 180 || direcao == 360) {
			// NAO MEXER NO (-120), SE NAO O ROBO VAI FICAR BATENDO NAS PAREDES, obrigado
		        ahead(altura - 120); 
		    } 
		    else {
		        ahead(largura - 120);
    }
    
    // Chegou perto da próxima quina? Vira 90 graus para fazer a curva perfeita
    turnRight(90);
}
}
        /**
         * onScannedRobot: What to do when you see another robot
         */
        public void onScannedRobot(ScannedRobotEvent e) {
            // Replace the next line with any behavior you would like
            fire(1);
        }

        /**
         * onHitByBullet: What to do when you're hit by a bullet
         */
        public void onHitByBullet(HitByBulletEvent e) {
            // Replace the next line with any behavior you would like
            back(10);
        }

        /**
         * onHitWall: What to do when you hit a wall
         */
        public void onHitWall(HitWallEvent e) {
            // Replace the next line with any behavior you would like
            back(20);
        }
    }

