package LucasMeloRobocod;

import java.awt.*;
import robocode.TeamRobot;
import robocode.ScannedRobotEvent;
import robocode.*;

public class LucasHPMeloBot extends TeamRobot
{
	int sentido = 1;	//Variável que determina o sentido que o Robô irá girar. '1' => Sentido Horario; '-1' => Sentido Anti-Horario. "Baseado no Robô RamFire."
	public void run() {
	
		setBodyColor(new Color(153,0,0)); 	//Vermelho Escuro
		setGunColor(new Color(0,0,153)); 	//Azul Escuro
		setRadarColor(new Color(0,153,0));	//Verde Escuro
		
		while(true) {
		
			areaCampo();				//Verifica se o robô esta fora da area segura.
			turnRight(30 * sentido);	//Rotaciona o robô em 30º no sentido determinado pelo método atualizaSentido(). "Baseado no Robô RamFire."
		}
	}


	public void onScannedRobot(ScannedRobotEvent e) {
				
		if(isTeammate(e.getName())){					//Verifica se o robô no radar é um aliado.
			return;
		}
		
		if(!e.isSentryRobot()){							//Verifica se o robô no radar é a Sentry, caso for da um giro para sair dela, se não entra no IF.
			atualizaSentido(e.getBearing());			//Atualiza o sentido de rotação usando como base o angulo do inimigo. "Baseado no Robô RamFire."
			turnRight(e.getBearing());					//Gira na direção do inimigo. "Baseado no Robô RamFire."
			
			if(e.getDistance() >=100){					//Verifica a distancia entrele eles, caso for menor de 100 pixels ele avança.
				ahead(e.getDistance()*0.7);				//Anda 70% da distancia entre eles para frente.
			}else{
							
				if (e.getEnergy() > 15) {				//Verifica a energia do inimigo, usa o tiro com potencia 3 para reduzir e potencia 2 para finalizar.
					fire(3);
				} else {
					fire(2);
				} 
											
				if(e.getDistance() < 50){				//Se a distancia entre eles for menor que 50 pixels, volta 50 pixels para trás, evitando perda de energia por colisão.
					back(50);	
				}			
			}
			scan();										//Dispara o scan() novamente para focar no inimigo. "Baseado no Robô RamFire."	
		}else{
			turnRight(45 * sentido);					//Rotaciona 45 graus no sentido determinado para tirar o radar da Sentry. 
		}			
	}


	public void onHitByBullet(HitByBulletEvent e) {
	
		return;
	}
	
	
	public void onHitWall(HitWallEvent e) {
		
		back(20);
	}	
	
	//Estrutura de IF e ELSE que fazem a checagem da posição atual do robô, e o faz retornar para a area segura se necessario.
	public void areaCampo(){
		if(getX()<=200 || getX()>=800 || getY()<=200 || getY()>=800){
			
			rotacionaCorpo();
			
			if(getX()<=200){
				if(getY()>=500){
					turnRight(135);
					ahead(200);
				}else{
					turnRight(45);
					ahead(200);
				}
			}else{
				if(getX()>=800){
					if(getY()>=500){
						turnRight(225);
						ahead(200);
					}else{
						turnRight(315);
						ahead(200);
					}
				}//fimse (getX()>=800)
			}//fimse (getX()<=200)
	
			if(getY()<=200){
				if(getX()>=500){
					turnRight(315);
					ahead(200);
				}else{
					turnRight(45);
					ahead(200);
				}
			}else{
				if(getY()>=800){
					if(getX()>=500){
						turnRight(225);
						ahead(200);
					}else{
						turnRight(135);
						ahead(200);
					}
				}//fimse (getY()>=800)
			}//fimse (getY()<=200)
		}
	}
	
	//Atualiza o sentido de rotação quando chamado. "Baseado no Robô RamFire."
	public void atualizaSentido(double graus){
		if (graus >= 0) {
			sentido = 1;
		} else {
			sentido = -1;
		}
	}
	
	//Determina qual o lado mais rapido de virar para o NORTE da arena e realiza a rotação.
	public void rotacionaCorpo(){
		if(getHeading() >= 180){
			turnRight(360 - getHeading());
		}else{
			turnLeft(getHeading());
		}
	}
	
}
