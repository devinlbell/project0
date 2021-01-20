import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;


import dev.lbell.daos.AccountDao;
import dev.lbell.daos.AccountDaoImpl;
import dev.lbell.daos.UserDao;
import dev.lbell.daos.UserDaoImpl;
import dev.lbell.models.BankAccount;
import dev.lbell.models.BankUser;
import dev.lbell.models.Customer;
import dev.lbell.models.Employee;
import dev.lbell.services.LoginService;

public class Project0Tests {
	
	private UserDao uDao = new UserDaoImpl();
	private AccountDao aDao = new AccountDaoImpl();
	
	
	@Test
	public void getCustomer() {
		BankUser user1 = uDao.getCustomer("MCTEST", "MCTEST@GMAIL.COM");
		BankUser user2 = new Customer(1, "MCTEST", "MCTEST@GMAIL.COM", 1);
		assertEquals(user1,  user2);
		
	}
	
	@Test
	public void testEmployeeLogin() {
		BankUser user1 = uDao.getEmployee("slim", "jim@gmail.com");
		BankUser user2 = new Employee(2, "slim", "jim@gmail.com");
		assertEquals(user1,  user2);
	}
	

	@Test
	public void testCustomerViewAccount() {
		List<BankAccount> accounts = aDao.getCustomerAccounts(25);
		BankAccount testAccount = new BankAccount(27, "PENDING", 3500, 25);
		assertEquals(accounts.get(2), testAccount);
	}
	
	@Test
	public void testDepositOrWithdraw() {
		assertTrue(aDao.makeTransaction(2, -23, 25, true));
	}
	

	@Test
	public void testApproveAccount() {
		assertTrue(aDao.addressAccount(1, "APPROVED"));
	}
	
	
}
