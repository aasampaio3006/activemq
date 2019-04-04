package br.org.jms.fila;

import java.util.Scanner;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class TesteProdutor {

	public static void main(String[] args) throws NamingException, JMSException {
		//InitialContext que por sua vez se baseia no arquivo de configuração jndi.properties:
		InitialContext context = new InitialContext();

		// imports do package javax.jms
		//fábrica não é criada e sim é feito um lookup dela:
		ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");
		Connection connection = factory.createConnection();
		//Inicializar conexao
		connection.start();
       
		//A Session no JMS abstrai o trabalho transacional e confirmação do recebimento da mensagem. 
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		
		
		// lugar concreto onde a mensagem será salvo temporariamente.
		Destination file = (Destination) context.lookup("financeiro");
		MessageProducer producer = session.createProducer(file);
		for (int i = 0; i < 1; i++) {
		Message message = session.createTextMessage("{\"id_candidato\":" + i + " \"palavra\":[37,183,639,569,713]}");
		producer.send(message);
		}
		
		
		new Scanner(System.in).nextLine(); // parar o programa para testar a conexao

		session.close();
		connection.close();
		context.close();

	}

}
