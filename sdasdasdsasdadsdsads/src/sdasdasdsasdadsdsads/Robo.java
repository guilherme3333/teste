package sdasdasdsasdadsdsads;

import lejos.nxt.*;

public class Robo {
	
	// Declara os motores
	private NXTRegulatedMotor m1 = new NXTRegulatedMotor(MotorPort.A); // Direita
	private NXTRegulatedMotor m2 = new NXTRegulatedMotor(MotorPort.B); // Esquerda
	
	//Declara os sensores de luz
	private LightSensor sensorEsquerda = new LightSensor(SensorPort.S3);
	private LightSensor sensorMeio = new LightSensor(SensorPort.S2);
	private LightSensor sensorDireita = new LightSensor(SensorPort.S1);
	
	// Declara o sensor de distância
	private UltrasonicSensor sensorDist = new UltrasonicSensor(SensorPort.S4);
	
	// Declara as constantes de velocidade
	private static final int SP = 250, branco = 35;
	
	public void menu(){
		
		calibrar();
		
		while(true){
			LCD.drawString("R TO RUN", 0, 0);
			LCD.drawString("L TO READ", 0, 1);
			LCD.drawString("ESC TO BREAK", 0, 2);
			Button.waitForAnyPress();
			LCD.clear();
			if(Button.RIGHT.isDown()){
				init();
			} else if(Button.LEFT.isDown()){
				leitura();
			} else if(Button.ESCAPE.isDown()){
				break;
			}
		}
	}
	
	private void seguir(){
		
		int dist;
		
		// Começa a seguir linha
		while(!Button.ESCAPE.isDown()){
			
			andar();
			
			if(!verificar(sensorMeio)){
				if(verificar(sensorDireita)){ // Se houver leitura na direita
					// Vire para a direita
					while(verificar(sensorDireita) && !verificar(sensorMeio)){
						viraDireita();
					}
				} else if(verificar(sensorEsquerda)){ // Se houver leitura na esquerda
					//Vire para a esquerda
					while(verificar(sensorEsquerda) && !verificar(sensorMeio)){
						viraEsquerda();
					}
				} 
			}
			
			dist = sensorDist.getDistance();
			
			if(dist <= 15){
				desvioDireita(dist);
			}
			
		}
		
		parar();
	}
	
	private boolean verificar(LightSensor sensor){
		// Verifica se a leitura do sensor recebido é de linha preta
		// Retorna verdadeiro se sim, e falso se não
		return sensor.getLightValue() < branco;
	}
	
	private void init(){
		// Seta a velocidade dos motores
		m1.setSpeed(SP);
		m2.setSpeed(SP);
		
		// Inicia o line following
		seguir();
	}
	
	private void andar(){
		// Anda para frente (apesar de os comandos estarem invertidos)
		m1.backward();
		m2.backward();
	}
	
	private void viraDireita(){
		m1.forward();
		m2.backward();
	}
	
	private void viraEsquerda(){
		m1.backward();
		m2.forward();
	}
	
	private void parar(){
		m1.stop();
		m2.stop();
	}
	
	private void calibrar(){
		// Calibra a cor branca
		LCD.drawString("BRANCO", 0, 0);
		Button.waitForAnyPress();
		sensorEsquerda.calibrateHigh();
		sensorMeio.calibrateHigh();
		sensorDireita.calibrateHigh();
		LCD.clear();
		
		// Calibra a cor preta
		LCD.drawString("PRETO", 0, 0);
		Button.waitForAnyPress();
		sensorEsquerda.calibrateLow();
		sensorMeio.calibrateLow();
		sensorDireita.calibrateLow();
		LCD.clear();
	}
	
	private void leitura(){
		int direita, esquerda, meio;
		
		while(!Button.ESCAPE.isDown()){
			direita = sensorDireita.getLightValue();
			meio = sensorMeio.getLightValue();
			esquerda = sensorEsquerda.getLightValue();
			
			try {
				LCD.drawInt(esquerda, 0, 0);
				LCD.drawInt(meio, 0, 1);
				LCD.drawInt(direita, 0, 2);
				Thread.sleep(100);
				LCD.clear();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	private void desvioDireita(int distancia){
		int deslocamento = distancia + 5;
		
		try {
			viraDireita();
			Thread.sleep(1250);
			andar();
			Thread.sleep(1250);
			viraEsquerda();
			Thread.sleep(1250);
			andar();
			Thread.sleep((deslocamento / 2) * 100);
			viraEsquerda();
			Thread.sleep(1250);
			andar();
			Thread.sleep(1250);
			viraDireita();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
