package Suvaqueira;
import robocode.*;
import java.awt.Color;


// API help : https://robocode.sourceforge.io/docs/robocode/robocode/Robot.html

    /**
     * SuvacoDoSaci - a robot by Felipe, Guilherme, Bernado Z
     */
    public class SuvacoDoSaci extends AdvancedRobot
    {
        // Variavel para guardar a energia anterior do safado e saber quando ele atirou
        private double energiaDoSafado = 100.0;

        // Sentido de marcha na parede: 1 = pra frente, -1 = marcha ré
        private int direcaoMovimento = 1;

        /**
         * run: SuvacoDoSaci's default behavior
         */
        public void run() {
		 setColors(Color.red,Color.blue,Color.red); // body,gun,radar
		//Liberta os movimentos do ROBO
		setAdjustRadarForRobotTurn(true);
		setAdjustGunForRobotTurn(true);
		setAdjustRadarForGunTurn(true);
		
		//Gira o radar 360 no inicio para achar alguem na largada
		setTurnRadarRight(360);
	

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
	// -------------------LOOP`----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------	

            // Robot main loop
            while(true) {
            // VARREDURA CONTINUA: manda o radar rodar sem parar. Se perder o lock, ele nunca fica cego!
            setTurnRadarRight(Double.POSITIVE_INFINITY);

            int direcao = (int) Math.round(getHeading()); 
		    if (direcao == 0 || direcao == 180 || direcao == 360) {
			// NAO MEXER NO (-120), SE NAO O ROBO VAI FICAR BATENDO NAS PAREDES, obrigado
		        ahead((altura - 120) * direcaoMovimento); 
		    } 
		    else {
		        ahead((largura - 120) * direcaoMovimento);
            }
    
            // Chegou perto da próxima quina. Vira 90 graus para fazer a curva perfeita
            turnRight(90);
            }
        }

        /**
         * onScannedRobot: What to do when you see another robot
         */
        public void onScannedRobot(ScannedRobotEvent e) {
//-----------------------------------DETECCAO DE TIRO E ESQUIVA------------------------------------
			// Se a energia dele caiu entre 0.1 e 3.0, ele acabou de atirar na nossa direcao!
			double variacaoEnergia = energiaDoSafado - e.getEnergy();
			if (variacaoEnergia >= 0.1 && variacaoEnergia <= 3.0) {
				// Inverte a marcha na parede imediatamente pra bala dele passar batida no ar
				direcaoMovimento = -direcaoMovimento;
				setAhead(100 * direcaoMovimento);
			}
			// Atualiza a vida dele pra comparar no proximo turno
			energiaDoSafado = e.getEnergy();

//-----------------------------------Radar lock----------------------------------------------------
			double anguloDoSafado = getHeadingRadians() + e.getBearingRadians(); // achando o safado que vai levar bala
			double giroDoRadar = robocode.util.Utils.normalRelativeAngle(anguloDoSafado - getRadarHeadingRadians());//caculando o quanto o radar tem que mexer para pegar o safado
			double chorinho = Math.signum(giroDoRadar) * (Math.PI / 18.0);	//Adciona um ""CHORINHO""  para dar bala bem no safado
			// Sobrescreve a varredura infinita com a trava instantanea em cima do alvo
			setTurnRadarRightRadians(giroDoRadar + chorinho);

//-----------------------------------CALCULANDO FORÇA DO TIRO--------------------------------------
			// Se tiver colado da paulada (3), se tiver longe economiza bala
			double distancia = e.getDistance();
			double forcaTiro = 400.0 / distancia;
			if (forcaTiro > 3.0) {
				forcaTiro = 3.0;
			} else if (forcaTiro < 1.0) {
				forcaTiro = 1.0;
			}

//-----------------------------------MIRA PREDITIVA (ADIVINHANDO ONDE ELE VAI ESTAR)----------------
			// Velocidade da bala depende da forca (regra do Robocode: 20 - 3 * forca)
			double velocidadeBala = 20.0 - (3.0 * forcaTiro);
			double tempoAteAcertar = distancia / velocidadeBala;

			// Prevendo onde o safado vai estar quando a bala chegar la
			double posicaoFuturaX = getX() + distancia * Math.sin(anguloDoSafado) + (e.getVelocity() * tempoAteAcertar * Math.sin(e.getHeadingRadians()));
			double posicaoFuturaY = getY() + distancia * Math.cos(anguloDoSafado) + (e.getVelocity() * tempoAteAcertar * Math.cos(e.getHeadingRadians()));

			// Trava para nao atirar na parede se o safado estiver quase batendo nela (arena tem 18px de margem)
			posicaoFuturaX = Math.max(18.0, Math.min(getBattleFieldWidth() - 18.0, posicaoFuturaX));
			posicaoFuturaY = Math.max(18.0, Math.min(getBattleFieldHeight() - 18.0, posicaoFuturaY));

			// Calculando o angulo exato pra onde o canhao tem que apontar
			double anguloPredito = Math.atan2(posicaoFuturaX - getX(), posicaoFuturaY - getY());
			double giroDoCanhao = robocode.util.Utils.normalRelativeAngle(anguloPredito - getGunHeadingRadians());

			// Gira o canhao pro futuro dele
			setTurnGunRightRadians(giroDoCanhao);

//-----------------------------------DISPARO SEM TRAVAR--------------------------------------------
			// So da tiro se a mira tiver quase perfeita no alvo e a arma nao tiver pelando de quente
			if (getGunHeat() == 0.0 && Math.abs(giroDoCanhao) < Math.toRadians(8)) {
				// TOMA SAFADO!!!!!!!!
				setFire(forcaTiro);
			}
        }

        /**
         * onHitByBullet: O que fazer quando tomar bala
         */
        public void onHitByBullet(HitByBulletEvent e) {
            // Inverte a direcao na parede pra nao tomar outro tiro no mesmo lugar
            direcaoMovimento = -direcaoMovimento;
            setAhead(80 * direcaoMovimento);

            // Gira o radar pro angulo de onde veio a bala pra achar o safado
            double anguloBala = e.getBearing();
            setTurnRadarRight(anguloBala);
        }

        /**
         * onHitWall: What to do when you hit a wall
         */
        public void onHitWall(HitWallEvent e) {
            // Se colidir na parede, recua e gira para retomar a rota
            back(40);
            turnRight(90);
        }

        /**
         * onHitRobot: Nao fica preso empurrando outro robo se trombar
         */
        public void onHitRobot(HitRobotEvent e) {
            if (e.isMyFault()) {
                // Inverte a marcha e gira um pouco para descolar da parede/robo
                direcaoMovimento = -direcaoMovimento;
                setAhead(100 * direcaoMovimento);
                setTurnRight(45);
            }
        }
    }