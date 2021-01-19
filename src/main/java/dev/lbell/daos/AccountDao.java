package dev.lbell.daos;

import java.util.List;

import dev.lbell.models.BankAccount;
import dev.lbell.models.Transactions;

public interface AccountDao {
	public List<BankAccount> getCustomerAccounts(int customerId);
	public boolean applyBankAccount(int customerId, double balance);
	public boolean addressAccount(int accountId, String status);
	public boolean makeTransaction(int accountId, double amount, double newBalance, String status);
	public List<Transactions> viewTransactions();
	public List<BankAccount> getUnapprovedAccts(int customerId, List<BankAccount> accts);

}
