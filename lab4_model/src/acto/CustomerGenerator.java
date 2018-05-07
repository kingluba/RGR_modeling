package acto;

import java.util.function.BooleanSupplier;

import model.TeamModel;
import presentation.TeamGUI;
import process.DispatcherFinishException;
import process.QueueForTransactions;
import qusystem.TransactionsGenerator;

public class CustomerGenerator extends TransactionsGenerator {
	/**
	 * link to the original object to use for cloning
	 */
	public Customer original = null;
	/*
	 * Умова максимального розміру черги
	 */
	private BooleanSupplier maxQueueSize;
	/*
	 * Максимальний роозмір черги
	 */
	private int maxQSize;
	/*
	 * Посилання на чергу замовників - вихідну чергу
	 */
	private QueueForTransactions<Customer> customersQueue;
	
	public CustomerGenerator(TeamGUI gui, TeamModel model) {
		super();
		setNameForProtocol("Генератор кліентів");
		maxQSize = gui.getCh_MaxQ().getInt();
		customersQueue = model.getCustomersQueueWaitingTeam();
	}
	/**
	 * method sets the original
	 * @param original
	 */
	public void setOriginal(Customer original) {
		this.original = original;
	}
	
	protected void createTransaction() {
		maxQueueSize = () -> customersQueue.size() < maxQSize;
		if (customersQueue.size() >= maxQSize) {
			try {
				waitForCondition(maxQueueSize,
						"поки черга не зменшиться");
			} catch (DispatcherFinishException e) {
				return;
			}
		}
		Customer client = (Customer) original.clone();
		client.setBirthTime(getDispatcher().getCurrentTime());
		client.setNameForProtocol("Клієнт № " + getDispatcher().getCurrentTime());
		getDispatcher().addStartingActor(client);
	}
}
