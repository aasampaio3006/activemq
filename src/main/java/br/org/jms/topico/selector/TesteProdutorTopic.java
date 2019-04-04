package br.org.jms.topico.selector;

import java.io.StringWriter;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.naming.InitialContext;
import javax.xml.bind.JAXB;

import br.org.jms.fila.modelo.Pedido;
import br.org.jms.fila.modelo.PedidoFactory;

public class TesteProdutorTopic {

	public static void main(String[] args) throws Exception {

				InitialContext context = new InitialContext();

				ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");
				//Connection connection = factory.createConnection();
				Connection connection = factory.createConnection("user", "senha");
				connection.start();
		       
				 
				Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);				
			
				Destination topico  = (Destination) context.lookup("loja");
				MessageProducer producer = session.createProducer(topico);
				
				Pedido pedido = new PedidoFactory().geraPedidoComValores();

//				StringWriter writer = new StringWriter();
//				JAXB.marshal(pedido, writer);
//				String xml = writer.toString();
//				Message message = session.createTextMessage(xml);
				
				for (int i = 5; i < 10; i++) {
					//Message message = session.createTextMessage("<pedido><id>00"+i+"</id><ebook>false<ebook></pedido>");
					//Message message = session.createTextMessage(xml);
					Message message = session.createObjectMessage(pedido);
					if (i % 2 == 0) {
						message.setBooleanProperty("ebook", true);
					}else {
						message.setBooleanProperty("ebook", false);
					}
					
			        producer.send(message);
					
				}    
		        
		        session.close();
		        connection.close();
		        context.close();

	}

}
