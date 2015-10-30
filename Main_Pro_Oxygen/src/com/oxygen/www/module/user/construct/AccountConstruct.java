package com.oxygen.www.module.user.construct;

import org.json.JSONException;
import org.json.JSONObject;
import com.oxygen.www.enties.Account;
public class AccountConstruct {
	public static Account ToAccountInfo(JSONObject c) {
		Account account = new Account();
		try {
			if (!c.isNull("account_id")) {
				account.account_id = c.getString("account_id");
			}
			if (!c.isNull("balance")) {
				account.balance = c.getString("balance");
			}
			if (!c.isNull("bank")) {
				account.bank = c.getString("bank");
			}
			if (!c.isNull("bank_no")) {
				account.bank_no = c.getString("bank_no");
			}
			if (!c.isNull("created_at")) {
				account.created_at = c.getString("created_at");
			}
			if (!c.isNull("created_by")) {
				account.created_by = c.getString("created_by");
			}
			if (!c.isNull("modified_at")) {
				account.modified_at = c.getString("modified_at");
			}
			if (!c.isNull("modified_by")) {
				account.modified_by = c.getString("modified_by");
			}
			if (!c.isNull("realname")) {
				account.realname = c.getString("realname");
			}
			if (!c.isNull("shop_id")) {
				account.shop_id = c.getString("shop_id");
			}
			if (!c.isNull("total_income")) {
				account.total_income = c.getString("total_income");
			}
			if (!c.isNull("user_id")) {
				account.user_id = c.getString("user_id");
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return account;
	}
}
