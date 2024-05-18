public class CoinSystem 
{
    private int coin1THB_Value  = 1; 
    private int coin2THB_Value  = 2; 
    private int coin5THB_Value  = 5;
    private int coin10THB_Value  = 10; 

    private int coin1THB_Amount;
    private int coin2THB_Amount;
    private int coin5THB_Amount;
    private int coin10THB_Amount;

    public enum Type 
    {
        Coin1THB,
        Coin2THB,
        Coin5THB,
        Coin10THB,
    }

    public CoinSystem()
    {
        this(0, 0, 0, 0);
    }

    public CoinSystem(CoinSystem coinSystem)
    {
        this(
            coinSystem.GetCoinAmount(Type.Coin1THB), 
            coinSystem.GetCoinAmount(Type.Coin2THB), 
            coinSystem.GetCoinAmount(Type.Coin5THB), 
            coinSystem.GetCoinAmount(Type.Coin10THB)
        );
    }

    public CoinSystem(int cash1THB, int cash2THB, int cash5THB, int cash10THB)
    {
        this.coin1THB_Amount = cash1THB;
        this.coin2THB_Amount = cash2THB;
        this.coin5THB_Amount = cash5THB;
        this.coin10THB_Amount = cash10THB;
    }

    public void InsertCoinAmount(Type coinType, int amount)
    {
        switch (coinType) 
        {
            case Coin1THB:
                InsertCoinAmount(amount, 0, 0, 0);
                break;

            case Coin2THB:
                InsertCoinAmount(0, amount, 0, 0);
                break;

            case Coin5THB:
                InsertCoinAmount(0, 0, amount, 0);    
                break;
                
            case Coin10THB:
                InsertCoinAmount(0, 0, 0, amount);    
                break;
        }

    }

    public void InsertCoinAmount(int coin1THB_Amount, int coin2THB_Amount, int coin5THB_Amount, int coin10THB_Amount)
    {
        this.coin1THB_Amount += coin1THB_Amount;
        this.coin2THB_Amount += coin2THB_Amount;
        this.coin5THB_Amount += coin5THB_Amount;
        this.coin10THB_Amount += coin10THB_Amount;
    }

    public void TakeCoinOut(Type coinType, int amount)
    {
        switch (coinType) 
        {
            case Coin1THB:
                TakeCoinOut(amount, 0, 0, 0);
                break;

            case Coin2THB:
                TakeCoinOut(0, amount, 0, 0);
                break;

            case Coin5THB:
                TakeCoinOut(0, 0, amount, 0);    
                break;
                
            case Coin10THB:
                TakeCoinOut(0, 0, 0, amount);    
                break;
        }

    }

    public void TakeCoinOut(int coin1THB_Amount, int coin2THB_Amount, int coin5THB_Amount, int coin10THB_Amount)
    {
        this.coin1THB_Amount -= coin1THB_Amount;
        this.coin2THB_Amount -= coin2THB_Amount;
        this.coin5THB_Amount -= coin5THB_Amount;
        this.coin10THB_Amount -= coin10THB_Amount;
    }

    private void SetCoinAmount(CoinSystem coinSystem)
    {
        SetCoinAmount(
            coinSystem.GetCoinAmount(Type.Coin1THB),
            coinSystem.GetCoinAmount(Type.Coin2THB),
            coinSystem.GetCoinAmount(Type.Coin5THB),
            coinSystem.GetCoinAmount(Type.Coin10THB)
        );
    }

    private void SetCoinAmount(int coin1THB_Amount, int coin2THB_Amount, int coin5THB_Amount, int coin10THB_Amount)
    {
        this.coin1THB_Amount = coin1THB_Amount;
        this.coin2THB_Amount = coin2THB_Amount;
        this.coin5THB_Amount = coin5THB_Amount;
        this.coin10THB_Amount = coin10THB_Amount;
    }

    public int GetCoinAmount(Type coinType)
    {
        switch (coinType) 
        {
            case Coin1THB:
                return coin1THB_Amount;

            case Coin2THB:
                return coin2THB_Amount;

            case Coin5THB:
                return coin5THB_Amount;
                
            case Coin10THB:   
                return coin10THB_Amount;
        }
        
        return 0;
    }

    public double GetTotalBalance()
    {
        return 
            (coin1THB_Amount * coin1THB_Value) +
            (coin2THB_Amount * coin2THB_Value) +
            (coin5THB_Amount * coin5THB_Value) +
            (coin10THB_Amount * coin10THB_Value);
    }

    public void TransferCoin(CoinSystem sender, CoinSystem receiver, CoinSystem coin)
    {
        receiver.InsertCoinAmount(
            coin.GetCoinAmount(Type.Coin1THB),
            coin.GetCoinAmount(Type.Coin2THB),
            coin.GetCoinAmount(Type.Coin5THB),
            coin.GetCoinAmount(Type.Coin10THB)
        );

        sender.TakeCoinOut(
            coin.GetCoinAmount(Type.Coin1THB),
            coin.GetCoinAmount(Type.Coin2THB),
            coin.GetCoinAmount(Type.Coin5THB),
            coin.GetCoinAmount(Type.Coin10THB)
        );
    }

    public void TransferCoin(CoinSystem receiver, CoinSystem coin)
    {
        TransferCoin(this, receiver, coin);
    }


    public void TransferAllCoin(CoinSystem sender, CoinSystem receiver)
    {
        TransferCoin(sender, receiver, new CoinSystem(
            sender.GetCoinAmount(Type.Coin1THB),
            sender.GetCoinAmount(Type.Coin2THB),
            sender.GetCoinAmount(Type.Coin5THB),
            sender.GetCoinAmount(Type.Coin10THB)
        ));
    }

    public void TransferAllCoin(CoinSystem receiver)
    {
        receiver.TransferAllCoin(this, receiver);
    }

    public boolean TryTransferCoin(CoinSystem receiver, double change)
    {
        return TryTransferCoin(this, receiver, change, new CoinSystem());
    }

    public boolean TryTransferCoin(CoinSystem receiver, double change, CoinSystem coinChange)
    {
        return TryTransferCoin(this, receiver, change, coinChange);
    }

    public boolean TryTransferCoin(CoinSystem sender, CoinSystem receiver, double change, CoinSystem coinChange)
    {
        CoinSystem tempSender = new CoinSystem(sender);
        CoinSystem tempReceiver = new CoinSystem(receiver);

        tempSender.TransferAllCoin(tempReceiver);
        
        if (tempReceiver.GetTotalBalance() < change)
        {
            // Fail not enough money to change
            return false;
        }

        int totalMoneyToChange = 0;
        int coin1THB_ToChange = 0;
        int coin2THB_ToChange = 0;
        int coin5THB_ToChange = 0;
        int coin10THB_ToChange = 0;

        // This for loop is work fine but it so pretty mess. if i have more time i could try to make it more read easier
        for (int i = 1; i <= tempReceiver.GetCoinAmount(Type.Coin10THB); i++) 
        {
            if (totalMoneyToChange + coin10THB_Value <= change)
            {
                coin10THB_ToChange++;
                totalMoneyToChange += coin10THB_Value;
                continue;
            }
            
            break;
        }

        for (int i = 1; i <= tempReceiver.GetCoinAmount(Type.Coin5THB); i++) 
        {
            if (totalMoneyToChange + coin5THB_Value <= change)
            {
                coin5THB_ToChange++;
                totalMoneyToChange += coin5THB_Value;
                continue;
            }
            
            break;
        }

        for (int i = 1; i <= tempReceiver.GetCoinAmount(Type.Coin2THB); i++) 
        {
            if (totalMoneyToChange + coin2THB_Value <= change)
            {
                coin1THB_ToChange++;
                totalMoneyToChange += coin2THB_Value;
                continue;
            }
            
            break;
        }

        for (int i = 1; i <= tempReceiver.GetCoinAmount(Type.Coin1THB); i++) 
        {
            if (totalMoneyToChange + coin1THB_Value <= change)
            {
                coin1THB_ToChange++;
                totalMoneyToChange += coin1THB_Value;
                continue;
            }
            
            break;
        }

        boolean hasEnough10THB_Coin = coin10THB_ToChange != 0;
        boolean hasEnough5THB_Coin = coin5THB_ToChange != 0;
        boolean hasEnough2THB_Coin = coin2THB_ToChange != 0;
        boolean hasEnough1THB_Coin = coin1THB_ToChange != 0;
        boolean hasEnoughForAllCoin = hasEnough10THB_Coin || hasEnough5THB_Coin || hasEnough2THB_Coin || hasEnough1THB_Coin;

        if (!hasEnoughForAllCoin && totalMoneyToChange != change)
        {
            // Fail not enough coin to change
            return false;
        }

        tempReceiver.TransferCoin(tempSender, new CoinSystem(
            coin1THB_ToChange,
            coin2THB_ToChange,
            coin5THB_ToChange,
            coin10THB_ToChange
        ));

        sender.SetCoinAmount(tempSender);
        receiver.SetCoinAmount(tempReceiver);

        coinChange.InsertCoinAmount(
            coin1THB_ToChange,
            coin2THB_ToChange,
            coin5THB_ToChange,
            coin10THB_ToChange
        );

        // Success Transfer
        return true;
    }

    public static CoinSystem.Type CashToCoinType(int cashType)
    {
        switch (cashType) 
        {
            case Constants.CURRENCY_1THB:
				return CoinSystem.Type.Coin1THB;

            case Constants.CURRENCY_2THB:
				return CoinSystem.Type.Coin2THB;

			case Constants.CURRENCY_5THB:
				return CoinSystem.Type.Coin5THB;

			case Constants.CURRENCY_10THB:
				return CoinSystem.Type.Coin10THB;
		}

		return null;
    }
  
     
}
