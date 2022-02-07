package application;

import java.util.HashMap;
import java.util.Map;

public class NepaliDigit {

	Map<String, Integer> digit = new HashMap<String, Integer>();

	public NepaliDigit() {
		digit.put(")", 0);
		digit.put("!", 1);
		digit.put("@", 2);
		digit.put("#", 3);
		digit.put("$", 4);
		digit.put("%", 5);
		digit.put("^", 6);
		digit.put("&", 7);
		digit.put("*", 8);
		digit.put("(", 9);
	}
	
	public Integer getNumberFromNepali(String Nepali) {
		return digit.get(Nepali);
	}
	
	public String getNepaliNumberFromNumber(Integer Number) {
		String Nepali = "";
		for(Map.Entry<String, Integer> entrySet : digit.entrySet()) {
			if(entrySet.getValue() == Number) {
				Nepali = entrySet.getKey();
				break;
			}
		}
		return Nepali;
	}
}
