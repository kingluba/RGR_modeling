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
	 * ����� ������������� ������ �����
	 */
	private BooleanSupplier maxQueueSize;
	/*
	 * ������������ ������ �����
	 */
	private int maxQSize;
	/*
	 * ��������� �� ����� ��������� - ������� �����
	 */
	private QueueForTransactions<Customer> customersQueue;
	
	public CustomerGenerator(TeamGUI gui, TeamModel model) {
		super();
		setNameForProtocol("��������� ������");
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
						"���� ����� �� ����������");
			} catch (DispatcherFinishException e) {
				return;
			}
		}
		Customer client = (Customer) original.clone();
		client.setBirthTime(getDispatcher().getCurrentTime());
		client.setNameForProtocol("�볺�� � " + getDispatcher().getCurrentTime());
		getDispatcher().addStartingActor(client);
	}
}
