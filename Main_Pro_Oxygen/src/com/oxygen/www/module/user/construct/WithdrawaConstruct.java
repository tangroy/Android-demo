package com.oxygen.www.module.user.construct;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.oxygen.www.enties.WithdrawaInfo;

public class WithdrawaConstruct {
	
	public static List<WithdrawaInfo> ToWithdrawaList(JSONArray jsonarray_data) {
		List<WithdrawaInfo> withdrawaInfos = new ArrayList<WithdrawaInfo>();
		for (int i = 0; i < jsonarray_data.length(); i++) {
			JSONObject c = (JSONObject) jsonarray_data.opt(i);
			withdrawaInfos.add(ToWithdrawaInfo(c));
		}
		return withdrawaInfos;
	}
	
	public static WithdrawaInfo ToWithdrawaInfo(JSONObject c) {
		WithdrawaInfo withdrawaInfo = new WithdrawaInfo();
		try {
			if (!c.isNull("account_id")) {
				withdrawaInfo.account_id = c.getString("account_id");
			}
			if (!c.isNull("amount")) {
				withdrawaInfo.amount = c.getString("amount");
			}
			if (!c.isNull("bank")) {
				withdrawaInfo.bank = c.getString("bank");
			}
			if (!c.isNull("bank_no")) {
				withdrawaInfo.bank_no = c.getString("bank_no");
			}
			if (!c.isNull("created_at")) {
				withdrawaInfo.created_at = c.getString("created_at");
			}
			if (!c.isNull("created_by")) {
				withdrawaInfo.created_by = c.getString("created_by");
			}
			if (!c.isNull("modified_at")) {
				withdrawaInfo.modified_at = c.getString("modified_at");
			}
			if (!c.isNull("modified_by")) {
				withdrawaInfo.modified_by = c.getString("modified_by");
			}
			if (!c.isNull("realname")) {
				withdrawaInfo.realname = c.getString("realname");
			}
			if (!c.isNull("id")) {
				withdrawaInfo.id = c.getString("id");
			}
			if (!c.isNull("status")) {
				withdrawaInfo.status = c.getString("status");
			}
			if (!c.isNull("order_id")) {
				withdrawaInfo.order_id = c.getString("order_id");
			}
			if (!c.isNull("memo")) {
				withdrawaInfo.memo = c.getString("memo");
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return withdrawaInfo;
	}
}
