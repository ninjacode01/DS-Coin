package DSCoinPackage;

public class TransactionQueue {

  public Transaction firstTransaction;
  public Transaction lastTransaction;
  public int numTransactions;

  public void AddTransactions (Transaction transaction) {
    if (this.numTransactions==0 || firstTransaction==null){
     
      this.firstTransaction = transaction;
      this.lastTransaction = transaction;
      this.firstTransaction.next=null;
      this.lastTransaction.next=null;
      this.numTransactions++;
    }
    else if (firstTransaction.next==null){
      lastTransaction=transaction;
      firstTransaction.next=transaction;
      lastTransaction.next=null;
      this.numTransactions++;
    } 
    else{
      lastTransaction.next=transaction;
      lastTransaction=transaction;
      this.numTransactions++;
    }
  }
  
  public Transaction RemoveTransaction () throws EmptyQueueException {
    
      if(this.firstTransaction==null){
        throw new EmptyQueueException();
      }
      numTransactions--;
      Transaction ans = new Transaction();
      ans.next= firstTransaction.next;
      ans.coinID=firstTransaction.coinID;
      ans.Source=firstTransaction.Source;
      ans.Destination=firstTransaction.Destination;
      ans.coinsrc_block=firstTransaction.coinsrc_block;

      this.firstTransaction= this.firstTransaction.next;

      return ans;
    } 
    
    

  public int size() {
    return this.numTransactions;
  }
}
