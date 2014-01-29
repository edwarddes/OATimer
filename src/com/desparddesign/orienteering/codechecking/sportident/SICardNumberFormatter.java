package com.desparddesign.orienteering.codechecking.sportident;

public class SICardNumberFormatter 
{
	static public int format(int card)
	{
		switch(((card & 0xff000000) >> 24))
		{
			case 0:
			{
				//SI5,SI6,SI6*
				if(((card & 0x00ff0000) >> 16) > 5)
				{
					//SI6
					return card;
				}
				else
				{
					if(((card & 0x00ff0000) >> 16) == 1) 
						return card & 0x0000ffff;
					else
						return ((card & 0x00ff0000) >> 16)*100000 + (card & 0x0000ffff);
				}
			}
			case 1:
			{
				//SI9
				return (card & 0x00ffffff);
			}
			case 2:
			{
				//SI8;
				return (card & 0x00ffffff);
			}
			case 4:
			{
				//pcard
			}
			case 6:
			{
				//tcard
			}
			case 0x0E:
			{
				//fcard
			}
			case 0x0F:
			{
				//SI10,SI11
				return (card & 0x00ffffff);
			}
		}
		return 0;
	}
	
	static public int unFormat(int cardNumber)
	{
		if(cardNumber < 500000)
		{
			if(cardNumber < 65001)
				return cardNumber;
			if(cardNumber < 265001)
				return (0x02 << 16) | (cardNumber-200000);
			if(cardNumber < 365001)
				return (0x03 << 16) | (cardNumber-300000);
			if(cardNumber < 465001)
				return (0x04 << 16) | (cardNumber-400000);
		}
		if(cardNumber < 1000000)
		{
			return (0x00 << 24) | cardNumber;
		}
		if(cardNumber < 2000000)
		{
			return (0x01 << 24) | cardNumber;
		}
		if(cardNumber < 3000000)
		{
			return (0x02 << 24) | cardNumber;
		}
		if(cardNumber > 7000000 && cardNumber < 8000000)
		{
			return (0x0F << 24) | cardNumber;
		}
		if(cardNumber > 9000000 && cardNumber < 10000000)
		{
			return (0x0F << 24) | cardNumber;
		}
		if(cardNumber > 16711680 && cardNumber < 16777215)
		{
			return (0x00 << 24) | cardNumber;
		}
		else
			return 0;
		//pCard
		//tCard
		//fCard
	}
	
	static public String kindOfCard(int cardNumber)
	{
		if(cardNumber < 500000)
			return "SI-5";
		if(cardNumber < 1000000)
			return "SI-6";
		if(cardNumber < 2000000)
			return "SI-9";
		if(cardNumber < 3000000)
			return "SI-8";
		if(cardNumber > 7000000 && cardNumber < 8000000)
			return "SI-10";
		if(cardNumber > 9000000 && cardNumber < 10000000)
			return "SI-11";
		//pCard
		//tCard
		//fCard
		//SI-6*
		return "Unknown";
	}
}
