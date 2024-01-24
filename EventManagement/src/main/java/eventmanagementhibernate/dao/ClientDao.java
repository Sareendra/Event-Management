package eventmanagementhibernate.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import eventmanagementhibernate.dto.Client;

public class ClientDao {
	
	EntityManagerFactory emf = Persistence.createEntityManagerFactory("amit");
	EntityManager em = emf.createEntityManager();
	EntityTransaction et = em.getTransaction();
	
	public Client saveClient (Client client) {
		
		et.begin();
		em.persist(client);
		et.commit();
		return client;
	}
	
	public Client findClient (int clientId) {
		
		Client client = em.find(Client.class, clientId);
		if(client!=null) {
			return client;
		}
		return null;
	}
	
	public Client updateClient (Client client, int id) {
		
		Client exClient = em.find(Client.class, id);
			if (exClient!=null) {
				client.setClientContact(id);
				et.begin();
				em.merge(client);
				et.commit();
				return client;
				
			}
			return null;
		
	}
	
	public Client deleteClient (int id) {
		
		Client exClient = em.find(Client.class, id);
		if (exClient!=null) {
			et.begin();
			em.remove(exClient);
			et.commit();
			return exClient;
			
		}
		return null;
	}
	

}
