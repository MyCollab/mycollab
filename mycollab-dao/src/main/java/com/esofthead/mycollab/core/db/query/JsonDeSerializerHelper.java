package com.esofthead.mycollab.core.db.query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.esofthead.mycollab.core.MyCollabException;
import com.esofthead.mycollab.core.utils.JsonDeSerializer;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * 
 * @author MyCollab Ltd.
 * @since 4.0
 * 
 */
public class JsonDeSerializerHelper {

	public static String toJson(List<SearchFieldInfo> fieldInfos) {
		return JsonDeSerializer.toJson(fieldInfos);
	}

	public static List<SearchFieldInfo> fromJson(String textVal) {
		Gson gson = new Gson();
		JsonParser parser = new JsonParser();
		JsonArray array = parser.parse(textVal).getAsJsonArray();
		List<SearchFieldInfo> list = new ArrayList<SearchFieldInfo>();
		for (int i = 0; i < array.size(); i++) {
			JsonElement element = array.get(i);
			JsonObject jsonObj = element.getAsJsonObject();

			JsonElement paramClsName = jsonObj.get("paramClsName");
			if (paramClsName != null) {
				String clsName = paramClsName.getAsString();
				SearchFieldInfo fieldInfo = new SearchFieldInfo();
				fieldInfo
						.setPrefixOper(jsonObj.get("prefixOper").getAsString());
				String compareOper = jsonObj.get("compareOper").getAsString();
				fieldInfo.setCompareOper(compareOper);
				fieldInfo.setParamClsName(clsName);

				if (StringParam.class.getName().equals(clsName)) {
					fieldInfo.setParam(gson.fromJson(jsonObj.get("param"),
							StringParam.class));
					fieldInfo.setValue(jsonObj.get("value").getAsString());
				} else if (NumberParam.class.getName().equals(clsName)) {
					fieldInfo.setParam(gson.fromJson(jsonObj.get("param"),
							NumberParam.class));
					fieldInfo.setValue(gson.fromJson(jsonObj.get("value"),
							Number.class));
				} else if (CompositionStringParam.class.getName().equals(
						clsName)) {
					fieldInfo.setParam(gson.fromJson(jsonObj.get("param"),
							CompositionStringParam.class));
					fieldInfo.setValue(gson.fromJson(jsonObj.get("value"),
							String.class));
				} else if (ConcatStringParam.class.getName().equals(clsName)) {
					fieldInfo.setParam(gson.fromJson(jsonObj.get("param"),
							ConcatStringParam.class));
					fieldInfo.setValue(jsonObj.get("value").getAsString());
				} else if (DateParam.class.getName().equals(clsName)) {
					fieldInfo.setParam(gson.fromJson(jsonObj.get("param"),
							DateParam.class));
					if (DateParam.BETWEEN.equals(compareOper)
							|| DateParam.NOT_BETWEEN.equals(compareOper)) {
						fieldInfo.setValue(gson.fromJson(jsonObj.get("value"),
								Date[].class));
					} else {
						fieldInfo.setValue(gson.fromJson(jsonObj.get("value"),
								Date.class));
					}
				} else if (PropertyParam.class.getName().equals(clsName)) {
					fieldInfo.setParam(gson.fromJson(jsonObj.get("param"),
							PropertyParam.class));
					fieldInfo.setValue(jsonObj.get("value").getAsString());
				} else if (PropertyListParam.class.getName().equals(clsName)) {
					fieldInfo.setParam(gson.fromJson(jsonObj.get("param"),
							PropertyListParam.class));
					fieldInfo.setValue(gson.fromJson(jsonObj.get("value"),
							Collection.class));
				} else if (StringListParam.class.getName().equals(clsName)) {
					fieldInfo.setParam(gson.fromJson(jsonObj.get("param"),
							StringListParam.class));
					fieldInfo.setValue(gson.fromJson(jsonObj.get("value"),
							List.class));
				} else {
					throw new MyCollabException("Invalid json value: "
							+ textVal);
				}
				list.add(fieldInfo);
			} else {
				throw new MyCollabException("Invalid json value: " + textVal);
			}
		}
		return list;
	}
}
