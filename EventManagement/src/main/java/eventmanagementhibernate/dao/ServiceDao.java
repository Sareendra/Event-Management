package eventmanagementhibernate.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import eventmanagementhibernate.dto.Admin;
import eventmanagementhibernate.dto.Service;

public class ServiceDao {
	EntityManagerFactory emf = Persistence.createEntityManagerFactory("amit");
	EntityManager em = emf.createEntityManager();
	EntityTransaction et = em.getTransaction();
	
	public Service saveService(Service service) {
		et.begin();
		em.persist(service);
		et.commit();
		return service;
	}
	public Service findService(int serviceid) {
		
		Service service = em.find(Service.class, serviceid);
		if (service!=null) {
			return service;	
		}
		
		return null;
	}
	public Service updateService(Service service,int id) {
		
		Service exService = em.find(Service.class, id);
			if (exService!=null) {
				service.setServiceId(id);
				et.begin();
				em.merge(service);
				et.commit();
				return service;
				
			}
			return null;
		}

	
	public Service deleteService(int id) {
		Service exService = em.find(Service.class, id);
		if (exService!=null) {
			et.begin();
			em.remove(exService);
			et.commit();
			return exService;
			
		}
		return null;
	}
	
	

}
