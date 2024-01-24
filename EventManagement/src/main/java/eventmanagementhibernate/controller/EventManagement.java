package eventmanagementhibernate.controller;

import java.security.PublicKey;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.criteria.From;

import org.hibernate.sql.Select;

import eventmanagementhibernate.dao.AdminDao;
import eventmanagementhibernate.dao.ClientDao;
import eventmanagementhibernate.dao.ClientEventDao;
import eventmanagementhibernate.dao.ClientServiceDao;
import eventmanagementhibernate.dao.ServiceDao;
import eventmanagementhibernate.dto.Admin;
import eventmanagementhibernate.dto.Client;
import eventmanagementhibernate.dto.ClientEvent;
import eventmanagementhibernate.dto.ClientService;
import eventmanagementhibernate.dto.EventType;
import eventmanagementhibernate.dto.Service;

public class EventManagement {
	
	
	AdminDao adao = new AdminDao();
	ServiceDao sdao = new ServiceDao();
	ClientDao cDao = new ClientDao();
	ClientEventDao ceDao =new ClientEventDao();
	ClientServiceDao csDao =new ClientServiceDao();
	
	
	
	
	
	public static void main(String[] args) {
		
//		EventManagement em = new EventManagement();
//		System.out.println(em.saveAdmin());
		
//		EventManagement em =new EventManagement();
//		System.out.println(em.adminLogin());
		
//		EventManagement eManagement = new EventManagement();
//		System.out.println(eManagement.saveService());
		
//		EventManagement eManagement = new EventManagement();
//		System.out.println(eManagement.updateService());
		
//		EventManagement eManagement = new EventManagement();
//		System.out.println(eManagement.deleteService());
		
//		EventManagement eManagement = new EventManagement();
//		System.out.println(eManagement.signupClient());
		
//		EventManagement eManagement = new EventManagement();
//		System.out.println(eManagement.clientlogin());
		
		EventManagement eManagement = new EventManagement();
		eManagement.createEvent();
	
		
		
	}
	public Admin saveAdmin() {
		Admin admin = new Admin();
		Scanner s = new Scanner(System.in);
		System.out.println("Enter admin name");
		admin.setAdminName(s.next());
		
		System.out.println("Enter admin mail");
		admin.setAdminMail(s.next());
		
		System.out.println("Enter admin password");
		admin.setAdminPassword(s.next());
		
		System.out.println("Enter admin contact number");
		admin.setAdminContact(s.nextLong());
		
		return adao.saveAdmin(admin);
		
	}
	
	
	
	
	public Admin adminLogin() {
		Scanner s = new Scanner(System.in);
		System.out.println("Enter admin email");
		String adminMail = s.next();
		
		System.out.println("Enter admin password");
		String adminPassword = s.next();
		
		Query query =Persistence.createEntityManagerFactory("amit").createEntityManager().createQuery("select a from Admin a where a.adminMail=?1");
		query.setParameter(1,adminMail);
		
		Admin exAdmin = (Admin) query.getSingleResult();
		
		if (exAdmin!=null) {
			if (exAdmin.getAdminPassword().equals(adminPassword)) {
				
				return exAdmin;
				
			}
			return null;
			
		}
		return null;
	}
	
	
	
	
	public Service saveService() {
		
		Admin exAdmin = adminLogin();
		
		if (exAdmin!=null) {
			Service service = new Service();
			Scanner s = new Scanner(System.in);
			System.out.println("Enter service name");
			service.setServiceName(s.next());
			
			System.out.println("Enter service cost per person");
			service.setServiceCostPerPerson(s.nextDouble());
			
			System.out.println("Enter service cost per day");
			service.setServiceCostPerDay(s.nextDouble());
			
			Service savedService = sdao.saveService(service);
			exAdmin.getServices().add(savedService);
			adao.updateAdmin(exAdmin, exAdmin.getAdminId());
			return service;
		}
		return  null;
		
		
	}
	
	public String updateService() {
		Scanner sc =new Scanner(System.in);
		Admin exAdmin = adminLogin();
		List<Service> services = exAdmin.getServices();
		
		
		for(Service s : services) {
			System.out.println("Service Id    "+"Service Name    "+"Cost_per_day    ");			
			System.out.println("    "+s.getServiceId()+"          "+s.getServiceName()+"           "+s.getServiceCostPerPerson());
		}
		
		System.out.println("**************************** Choose service Id from above to update ************************************");
		
		int id = sc.nextInt();
		Service tobeUpdated = sdao.findService(id);
		
		System.out.println("Enter updated cost per person" );
		double costperPerson = sc.nextDouble();
		
		System.out.println("Enter updated cost per day");
		double costperday = sc.nextDouble();
		
		
		tobeUpdated.setServiceCostPerPerson(costperPerson);
		tobeUpdated.setServiceCostPerDay(costperday);
		
		Service updated = sdao.updateService(tobeUpdated, id);
		if(updated!=null) {
			return "service updated sucess";
			
		}
		return "Invalid service Id";
		
	}
	
	public String deleteService() {
		
		Scanner sc = new Scanner(System.in);
		Admin exAdmin = adminLogin();
		
		if(exAdmin!=null) {
			List<Service> services = exAdmin.getServices();
			System.out.println("**************************** Choose service Id from above to update ************************************");
			
			for(Service s: services) {
				System.out.println("serivce ID         +"+"serivce Name         "+"cost_per_person        "+"cost_per_day");
				System.out.println("      "+s.getServiceId()+"             "+s.getServiceName()+"              "+s.getServiceCostPerPerson()+"              "+s.getServiceCostPerDay());
				
			}
			
			int id = sc.nextInt();
			
			List<Service> newlist =new ArrayList<Service>();
			
			for(Service s: services) {
				if(s.getServiceId()!=id) {
					newlist.add(s);
				}
			}
			
			exAdmin.setServices(newlist);
			adao.updateAdmin(exAdmin, exAdmin.getAdminId());
			sdao.deleteService(id);
		}
		
		
		return "service deleted sucess";
		
		
		
	}
	
	public List<Service> getAlListService(){
//		System.out.println("Enter Admin Credentials to proceed ");
//		Admin exAdmin = adminLogin();
//		
//		if(exAdmin != null) {
//			
//			
//		}
//		return null;
		
		Query query = Persistence.createEntityManagerFactory("amit").createEntityManager().createQuery("select s from Service s");
		List<Service> services = query.getResultList();
		
		return services;
	}
	
	
	public Client signupClient() {
		
		Client client = new Client();
		Scanner s = new Scanner(System.in);
		System.out.println("Enter client Name:");
		client.setClientName(s.next());
		
		System.out.println("Enter client Contact");
		client.setClientContact(s.nextLong());
		
		System.out.println("Enter client Mail");
		client.setClientMail(s.next());
		
		System.out.println("Enter Client Password");
		client.setClientPassword(s.next());
		
		return cDao.saveClient(client);
	}
	
	
	public Client clientlogin() {
		
		Scanner s = new Scanner(System.in);
		
		System.out.println("Enter client email");
		String clientMail = s.next();
		
		System.out.println("Enter client password");
		String clientPassword = s.next();
		
		Query query = Persistence.createEntityManagerFactory("amit").createEntityManager().createQuery("select c from Client c where c.clientMail=?1");
		query.setParameter(1, clientMail);
		
		Client exClient = (Client)query.getSingleResult();
		
		if (exClient!=null) {
			
			if (exClient!=null) {
				
				if(exClient.getClientPassword().equals(clientPassword)) {
					return exClient;
				}
				return null;
				
				
			}
			
		}
		return null;
		
	}
	
	
	
	
	
	
	public void createEvent() {
		
		Scanner sc = new Scanner(System.in);
		
		Client exClient = clientlogin();
		
		if(exClient!=null) {	
			EventType[] eventList= EventType.values();
			
			int No=1;
			for(EventType eve : eventList) {
				
				
				System.out.println(No+". "+eve);
				No++;
		
			}
			
			List<ClientEvent> cevents = new ArrayList<ClientEvent>();
			ClientEvent ce  = new ClientEvent();
			
			
			System.out.println("Choose the type of event:");
			
			int opt = sc.nextInt();
			
			switch (opt) {
			case 1:
				System.out.println("Enter the number of people: ");
				ce.setClientEventNoOfPeople(sc.nextInt());
				
				System.out.println("Enter the No.of days");
				ce.setClientEventNoOfDays(sc.nextInt());
				
				System.out.println("Enter start date");
				ce.setStartDate(null);
				
				System.out.println("Enter event location");
				ce.setClientEventLocation(sc.next());
				
//				ce.setClient(exClient);
//				ce.setClientServices(listOfService());		
				ce.setEventType(EventType.Marriage);
				
				
				ceDao.saveClientEvent(ce);
				exClient.getEvents().add(ce);
				cDao.updateClient(exClient, exClient.getClientId());
	
				break;
			case 2:
				System.out.println("Enter the number of people: ");
				ce.setClientEventNoOfPeople(sc.nextInt());
				
				System.out.println("Enter the No.of days");
				ce.setClientEventNoOfDays(sc.nextInt());
				
				System.out.println("Enter start date");
				ce.setStartDate(null);
				
				System.out.println("Enter event location");
				ce.setClientEventLocation(sc.next());
				
				ce.setClient(exClient);
				ce.setClientServices(listOfService());
				ce.setEventType(EventType.Engagement);
				
				
				ceDao.saveClientEvent(ce);
				exClient.getEvents().add(ce);
				cDao.updateClient(exClient, exClient.getClientId());
				break;
				
			case 3:
				System.out.println("Enter the number of people: ");
				ce.setClientEventNoOfPeople(sc.nextInt());
				
				System.out.println("Enter the No.of days");
				ce.setClientEventNoOfDays(sc.nextInt());
				
				System.out.println("Enter start date");
				ce.setStartDate(null);
				
				System.out.println("Enter event location");
				ce.setClientEventLocation(sc.next());
				
				ce.setClient(exClient);
				ce.setClientServices(listOfService());
				ce.setEventType(EventType.Birthday);
				
				
				ceDao.saveClientEvent(ce);
				exClient.getEvents().add(ce);
				cDao.updateClient(exClient, exClient.getClientId());
				break;
			case 4:
				System.out.println("Enter the number of people: ");
				ce.setClientEventNoOfPeople(sc.nextInt());
				
				System.out.println("Enter the No.of days");
				ce.setClientEventNoOfDays(sc.nextInt());
				
				System.out.println("Enter start date");
				ce.setStartDate(null);
				
				System.out.println("Enter event location");
				ce.setClientEventLocation(sc.next());
				
				ce.setClient(exClient);
				ce.setClientServices(listOfService());
				ce.setEventType(EventType.Anniversary);
				
				
				ceDao.saveClientEvent(ce);
				exClient.getEvents().add(ce);
				cDao.updateClient(exClient, exClient.getClientId());
				break;
				
			case 5:
				System.out.println("Enter the number of people: ");
				ce.setClientEventNoOfPeople(sc.nextInt());
				
				System.out.println("Enter the No.of days");
				ce.setClientEventNoOfDays(sc.nextInt());
				
				System.out.println("Enter start date");
				ce.setStartDate(null);
				
				System.out.println("Enter event location");
				ce.setClientEventLocation(sc.next());
				
				ce.setClient(exClient);
				ce.setClientServices(listOfService());
				ce.setEventType(EventType.BabyShower);
				
				
				ceDao.saveClientEvent(ce);
				exClient.getEvents().add(ce);
				cDao.updateClient(exClient, exClient.getClientId());
			
			case 6:
				System.out.println("Enter the number of people: ");
				ce.setClientEventNoOfPeople(sc.nextInt());
				
				System.out.println("Enter the No.of days");
				ce.setClientEventNoOfDays(sc.nextInt());
				
				System.out.println("Enter start date");
				ce.setStartDate(null);
				
				System.out.println("Enter event location");
				ce.setClientEventLocation(sc.next());
				
				ce.setClient(exClient);
				ce.setClientServices(listOfService());
				ce.setEventType(EventType.Reunion);
				
				
				ceDao.saveClientEvent(ce);
				exClient.getEvents().add(ce);
				cDao.updateClient(exClient, exClient.getClientId());
				
				break;
				
			case 7:
				System.out.println("Enter the number of people: ");
				ce.setClientEventNoOfPeople(sc.nextInt());
				
				System.out.println("Enter the No.of days");
				ce.setClientEventNoOfDays(sc.nextInt());
				
				System.out.println("Enter start date");
				
				ce.setStartDate(null);
				
				System.out.println("Enter event location");
				ce.setClientEventLocation(sc.next());
				
				ce.setClient(exClient);
				ce.setClientServices(listOfService());
				ce.setEventType(EventType.NamingCeremony);
				
				
				ceDao.saveClientEvent(ce);
				exClient.getEvents().add(ce);
				cDao.updateClient(exClient, exClient.getClientId());
				break;
				
			case 8:
				System.out.println("Enter the number of people: ");
				ce.setClientEventNoOfPeople(sc.nextInt());
				
				System.out.println("Enter the No.of days");
				ce.setClientEventNoOfDays(sc.nextInt());
				
				System.out.println("Enter start date");
				ce.setStartDate(null);
				
				
				System.out.println("Enter event location");
				ce.setClientEventLocation(sc.next());
				
				ce.setClient(exClient);
				ce.setClientServices(listOfService());
				ce.setEventType(EventType.BachelorParty);
				
				
				ceDao.saveClientEvent(ce);
				exClient.getEvents().add(ce);
				cDao.updateClient(exClient, exClient.getClientId());
				break;
				
		

			default:
				System.out.println("Enter the correct choose...!!");
				break;
			}
		
					
		}
		
	}
	
	public List<ClientService> listOfService(){
		Scanner sc = new Scanner(System.in);
		List<ClientService> cslist = new ArrayList<ClientService>();
		
		List<Service> lServices = getAlListService();
		
		int count =1;
		for(Service s: lServices) {
			System.out.println(count+". "+s.getServiceName()+" "+s.getServiceCostPerPerson()+" "+s.getServiceCostPerDay());
			count++;
		}
		
		return cslist;
		
		
		
	}


	

}
