/**
 * This file is part of mycollab-core.
 *
 * mycollab-core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-core.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.core.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import com.esofthead.mycollab.core.MyCollabException;

/**
 * Class keep all timezones of system
 * 
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class TimezoneMapper {
	public static Map<String, TimezoneExt> timeMap;

	public static String AREAS[] = new String[] { "UTC", "Africa", "America",
			"Atlantic", "Antarctica", "Asia", "Australia", "Europe", "Indian",
			"Pacific" };

	static {
		timeMap = new HashMap<String, TimezoneMapper.TimezoneExt>();

		timeMap.put("3", new TimezoneExt("3", "(GMT-11:00) Midway", "Pacific",
				"Pacific/Midway"));
		timeMap.put("4", new TimezoneExt("4", "(GMT-11:00) Niue", "Pacific",
				"Pacific/Niue"));
		timeMap.put("5", new TimezoneExt("5", "(GMT-11:00) Pago Pago",
				"Pacific", "Pacific/Pago_Pago"));
		timeMap.put("6", new TimezoneExt("6", "(GMT-11:00) Samoa", "Pacific",
				"Pacific/Samoa"));
		timeMap.put("8", new TimezoneExt("8", "(GMT-10:00) Adak", "America",
				"America/Adak"));
		timeMap.put("9", new TimezoneExt("9", "(GMT-10:00) Atka", "America",
				"America/Atka"));
		timeMap.put("12", new TimezoneExt("12", "(GMT-10:00) Fakaofo",
				"Pacific", "Pacific/Fakaofo"));
		timeMap.put("13", new TimezoneExt("13", "(GMT-10:00) Honolulu",
				"Pacific", "Pacific/Honolulu"));
		timeMap.put("14", new TimezoneExt("14", "(GMT-10:00) Johnston",
				"Pacific", "Pacific/Johnston"));
		timeMap.put("15", new TimezoneExt("15", "(GMT-10:00) Rarotonga",
				"Pacific", "Pacific/Rarotonga"));
		timeMap.put("16", new TimezoneExt("16", "(GMT-10:00) Tahiti",
				"Pacific", "Pacific/Tahiti"));
		timeMap.put("20", new TimezoneExt("20", "(GMT-09:00) Marquesas",
				"Pacific", "Pacific/Marquesas"));
		timeMap.put("22", new TimezoneExt("22", "(GMT-09:00) Anchorage",
				"America", "America/Anchorage"));
		timeMap.put("23", new TimezoneExt("23", "(GMT-09:00) Juneau",
				"America", "America/Juneau"));
		timeMap.put("24", new TimezoneExt("24", "(GMT-09:00) Nome", "America",
				"America/Nome"));
		timeMap.put("25", new TimezoneExt("25", "(GMT-09:00) Sitka", "America",
				"America/Sitka"));
		timeMap.put("26", new TimezoneExt("26", "(GMT-09:00) Yakutat",
				"America", "America/Yakutat"));
		timeMap.put("28", new TimezoneExt("28", "(GMT-09:00) Gambier",
				"Pacific", "Pacific/Gambier"));
		timeMap.put("32", new TimezoneExt("32", "(GMT-08:00) Dawson",
				"America", "America/Dawson"));
		timeMap.put("33", new TimezoneExt("33", "(GMT-08:00) Ensenada",
				"America", "America/Ensenada"));
		timeMap.put("34", new TimezoneExt("34", "(GMT-08:00) Los Angeles",
				"America", "America/Los_Angeles"));
		timeMap.put("35", new TimezoneExt("35", "(GMT-08:00) Metlakatla",
				"America", "America/Metlakatla"));
		timeMap.put("36", new TimezoneExt("36", "(GMT-08:00) Santa Isabel",
				"America", "America/Santa_Isabel"));
		timeMap.put("37", new TimezoneExt("37", "(GMT-08:00) Tijuana",
				"America", "America/Tijuana"));
		timeMap.put("38", new TimezoneExt("38", "(GMT-08:00) Vancouver",
				"America", "America/Vancouver"));
		timeMap.put("39", new TimezoneExt("39", "(GMT-08:00) Whitehorse",
				"America", "America/Whitehorse"));
		timeMap.put("46", new TimezoneExt("46", "(GMT-08:00) Pitcairn",
				"Pacific", "Pacific/Pitcairn"));
		timeMap.put("51", new TimezoneExt("51", "(GMT-07:00) Boise", "America",
				"America/Boise"));
		timeMap.put("52", new TimezoneExt("52", "(GMT-07:00) Cambridge Bay",
				"America", "America/Cambridge_Bay"));
		timeMap.put("53", new TimezoneExt("53", "(GMT-07:00) Chihuahua",
				"America", "America/Chihuahua"));
		timeMap.put("54", new TimezoneExt("54", "(GMT-07:00) Dawson Creek",
				"America", "America/Dawson_Creek"));
		timeMap.put("55", new TimezoneExt("55", "(GMT-07:00) Denver",
				"America", "America/Denver"));
		timeMap.put("56", new TimezoneExt("56", "(GMT-07:00) Edmonton",
				"America", "America/Edmonton"));
		timeMap.put("57", new TimezoneExt("57", "(GMT-07:00) Hermosillo",
				"America", "America/Hermosillo"));
		timeMap.put("58", new TimezoneExt("58", "(GMT-07:00) Inuvik",
				"America", "America/Inuvik"));
		timeMap.put("59", new TimezoneExt("59", "(GMT-07:00) Mazatlan",
				"America", "America/Mazatlan"));
		timeMap.put("60", new TimezoneExt("60", "(GMT-07:00) Ojinaga",
				"America", "America/Ojinaga"));
		timeMap.put("61", new TimezoneExt("61", "(GMT-07:00) Phoenix",
				"America", "America/Phoenix"));
		timeMap.put("62", new TimezoneExt("62", "(GMT-07:00) Shiprock",
				"America", "America/Shiprock"));
		timeMap.put("63", new TimezoneExt("63", "(GMT-07:00) Yellowknife",
				"America", "America/Yellowknife"));
		timeMap.put("75", new TimezoneExt("75", "(GMT-06:00) Bahia Banderas",
				"America", "America/Bahia_Banderas"));
		timeMap.put("76", new TimezoneExt("76", "(GMT-06:00) Belize",
				"America", "America/Belize"));
		timeMap.put("77", new TimezoneExt("77", "(GMT-06:00) Cancun",
				"America", "America/Cancun"));
		timeMap.put("78", new TimezoneExt("78", "(GMT-06:00) Chicago",
				"America", "America/Chicago"));
		timeMap.put("79", new TimezoneExt("79", "(GMT-06:00) Costa Rica",
				"America", "America/Costa_Rica"));
		timeMap.put("80", new TimezoneExt("80", "(GMT-06:00) El Salvador",
				"America", "America/El_Salvador"));
		timeMap.put("81", new TimezoneExt("81", "(GMT-06:00) Guatemala",
				"America", "America/Guatemala"));
		timeMap.put("82", new TimezoneExt("82", "(GMT-06:00) Indiana/Knox",
				"America", "America/Indiana/Knox"));
		timeMap.put("83", new TimezoneExt("83",
				"(GMT-06:00) Indiana/Tell City", "America",
				"America/Indiana/Tell_City"));
		timeMap.put("84", new TimezoneExt("84", "(GMT-06:00) Knox IN",
				"America", "America/Knox_IN"));
		timeMap.put("85", new TimezoneExt("85", "(GMT-06:00) Managua",
				"America", "America/Managua"));
		timeMap.put("86", new TimezoneExt("86", "(GMT-06:00) Matamoros",
				"America", "America/Matamoros"));
		timeMap.put("87", new TimezoneExt("87", "(GMT-06:00) Menominee",
				"America", "America/Menominee"));
		timeMap.put("88", new TimezoneExt("88", "(GMT-06:00) Merida",
				"America", "America/Merida"));
		timeMap.put("89", new TimezoneExt("89", "(GMT-06:00) Mexico City",
				"America", "America/Mexico_City"));
		timeMap.put("90", new TimezoneExt("90", "(GMT-06:00) Monterrey",
				"America", "America/Monterrey"));
		timeMap.put("91", new TimezoneExt("91",
				"(GMT-06:00) North Dakota/Beulah", "America",
				"America/North_Dakota/Beulah"));
		timeMap.put("92", new TimezoneExt("92",
				"(GMT-06:00) North Dakota/Center", "America",
				"America/North_Dakota/Center"));
		timeMap.put("93", new TimezoneExt("93",
				"(GMT-06:00) North Dakota/New Salem", "America",
				"America/North_Dakota/New_Salem"));
		timeMap.put("94", new TimezoneExt("94", "(GMT-06:00) Rainy River",
				"America", "America/Rainy_River"));
		timeMap.put("95", new TimezoneExt("95", "(GMT-06:00) Rankin Inlet",
				"America", "America/Rankin_Inlet"));
		timeMap.put("96", new TimezoneExt("96", "(GMT-06:00) Regina",
				"America", "America/Regina"));
		timeMap.put("97", new TimezoneExt("97", "(GMT-06:00) Resolute",
				"America", "America/Resolute"));
		timeMap.put("98", new TimezoneExt("98", "(GMT-06:00) Swift Current",
				"America", "America/Swift_Current"));
		timeMap.put("99", new TimezoneExt("99", "(GMT-06:00) Tegucigalpa",
				"America", "America/Tegucigalpa"));
		timeMap.put("100", new TimezoneExt("100", "(GMT-06:00) Winnipeg",
				"America", "America/Winnipeg"));
		timeMap.put("109", new TimezoneExt("109", "(GMT-06:00) Easter",
				"Pacific", "Pacific/Easter"));
		timeMap.put("110", new TimezoneExt("110", "(GMT-06:00) Galapagos",
				"Pacific", "Pacific/Galapagos"));
		timeMap.put("115", new TimezoneExt("115", "(GMT-05:00) Atikokan",
				"America", "America/Atikokan"));
		timeMap.put("116", new TimezoneExt("116", "(GMT-05:00) Bogota",
				"America", "America/Bogota"));
		timeMap.put("117", new TimezoneExt("117", "(GMT-05:00) Cayman",
				"America", "America/Cayman"));
		timeMap.put("118", new TimezoneExt("118", "(GMT-05:00) Coral Harbour",
				"America", "America/Coral_Harbour"));
		timeMap.put("119", new TimezoneExt("119", "(GMT-05:00) Detroit",
				"America", "America/Detroit"));
		timeMap.put("120", new TimezoneExt("120", "(GMT-05:00) Fort Wayne",
				"America", "America/Fort_Wayne"));
		timeMap.put("121", new TimezoneExt("121", "(GMT-05:00) Grand Turk",
				"America", "America/Grand_Turk"));
		timeMap.put("122", new TimezoneExt("122", "(GMT-05:00) Guayaquil",
				"America", "America/Guayaquil"));
		timeMap.put("123", new TimezoneExt("123", "(GMT-05:00) Havana",
				"America", "America/Havana"));
		timeMap.put("124", new TimezoneExt("124",
				"(GMT-05:00) Indiana/Indianapolis", "America",
				"America/Indiana/Indianapolis"));
		timeMap.put("125", new TimezoneExt("125",
				"(GMT-05:00) Indiana/Marengo", "America",
				"America/Indiana/Marengo"));
		timeMap.put("126", new TimezoneExt("126",
				"(GMT-05:00) Indiana/Petersburg", "America",
				"America/Indiana/Petersburg"));
		timeMap.put("127", new TimezoneExt("127", "(GMT-05:00) Indiana/Vevay",
				"America", "America/Indiana/Vevay"));
		timeMap.put("128", new TimezoneExt("128",
				"(GMT-05:00) Indiana/Vincennes", "America",
				"America/Indiana/Vincennes"));
		timeMap.put("129", new TimezoneExt("129",
				"(GMT-05:00) Indiana/Winamac", "America",
				"America/Indiana/Winamac"));
		timeMap.put("130", new TimezoneExt("130", "(GMT-05:00) Indianapolis",
				"America", "America/Indianapolis"));
		timeMap.put("131", new TimezoneExt("131", "(GMT-05:00) Iqaluit",
				"America", "America/Iqaluit"));
		timeMap.put("132", new TimezoneExt("132", "(GMT-05:00) Jamaica",
				"America", "America/Jamaica"));
		timeMap.put("133", new TimezoneExt("133",
				"(GMT-05:00) Kentucky/Louisville", "America",
				"America/Kentucky/Louisville"));
		timeMap.put("134", new TimezoneExt("134",
				"(GMT-05:00) Kentucky/Monticello", "America",
				"America/Kentucky/Monticello"));
		timeMap.put("135", new TimezoneExt("135", "(GMT-05:00) Lima",
				"America", "America/Lima"));
		timeMap.put("136", new TimezoneExt("136", "(GMT-05:00) Louisville",
				"America", "America/Louisville"));
		timeMap.put("137", new TimezoneExt("137", "(GMT-05:00) Montreal",
				"America", "America/Montreal"));
		timeMap.put("138", new TimezoneExt("138", "(GMT-05:00) Nassau",
				"America", "America/Nassau"));
		timeMap.put("139", new TimezoneExt("139", "(GMT-05:00) New York",
				"America", "America/New_York"));
		timeMap.put("140", new TimezoneExt("140", "(GMT-05:00) Nipigon",
				"America", "America/Nipigon"));
		timeMap.put("141", new TimezoneExt("141", "(GMT-05:00) Panama",
				"America", "America/Panama"));
		timeMap.put("142", new TimezoneExt("142", "(GMT-05:00) Pangnirtung",
				"America", "America/Pangnirtung"));
		timeMap.put("143", new TimezoneExt("143", "(GMT-05:00) Port-au-Prince",
				"America", "America/Port-au-Prince"));
		timeMap.put("144", new TimezoneExt("144", "(GMT-05:00) Thunder Bay",
				"America", "America/Thunder_Bay"));
		timeMap.put("145", new TimezoneExt("145", "(GMT-05:00) Toronto",
				"America", "America/Toronto"));
		timeMap.put("158", new TimezoneExt("158", "(GMT-04:00) Caracas",
				"America", "America/Caracas"));
		timeMap.put("159", new TimezoneExt("159", "(GMT-04:00) Anguilla",
				"America", "America/Anguilla"));
		timeMap.put("160", new TimezoneExt("160", "(GMT-04:00) Antigua",
				"America", "America/Antigua"));
		timeMap.put("161", new TimezoneExt("161",
				"(GMT-04:00) Argentina/San Luis", "America",
				"America/Argentina/San_Luis"));
		timeMap.put("162", new TimezoneExt("162", "(GMT-04:00) Aruba",
				"America", "America/Aruba"));
		timeMap.put("163", new TimezoneExt("163", "(GMT-04:00) Asuncion",
				"America", "America/Asuncion"));
		timeMap.put("164", new TimezoneExt("164", "(GMT-04:00) Barbados",
				"America", "America/Barbados"));
		timeMap.put("165", new TimezoneExt("165", "(GMT-04:00) Blanc-Sablon",
				"America", "America/Blanc-Sablon"));
		timeMap.put("166", new TimezoneExt("166", "(GMT-04:00) Boa Vista",
				"America", "America/Boa_Vista"));
		timeMap.put("167", new TimezoneExt("167", "(GMT-04:00) Campo Grande",
				"America", "America/Campo_Grande"));
		timeMap.put("168", new TimezoneExt("168", "(GMT-04:00) Cuiaba",
				"America", "America/Cuiaba"));
		timeMap.put("169", new TimezoneExt("169", "(GMT-04:00) Curacao",
				"America", "America/Curacao"));
		timeMap.put("170", new TimezoneExt("170", "(GMT-04:00) Dominica",
				"America", "America/Dominica"));
		timeMap.put("171", new TimezoneExt("171", "(GMT-04:00) Eirunepe",
				"America", "America/Eirunepe"));
		timeMap.put("172", new TimezoneExt("172", "(GMT-04:00) Glace Bay",
				"America", "America/Glace_Bay"));
		timeMap.put("173", new TimezoneExt("173", "(GMT-04:00) Goose Bay",
				"America", "America/Goose_Bay"));
		timeMap.put("174", new TimezoneExt("174", "(GMT-04:00) Grenada",
				"America", "America/Grenada"));
		timeMap.put("175", new TimezoneExt("175", "(GMT-04:00) Guadeloupe",
				"America", "America/Guadeloupe"));
		timeMap.put("176", new TimezoneExt("176", "(GMT-04:00) Guyana",
				"America", "America/Guyana"));
		timeMap.put("177", new TimezoneExt("177", "(GMT-04:00) Halifax",
				"America", "America/Halifax"));
		timeMap.put("178", new TimezoneExt("178", "(GMT-04:00) Kralendijk",
				"America", "America/Kralendijk"));
		timeMap.put("179", new TimezoneExt("179", "(GMT-04:00) La Paz",
				"America", "America/La_Paz"));
		timeMap.put("180", new TimezoneExt("180", "(GMT-04:00) Lower Princes",
				"America", "America/Lower_Princes"));
		timeMap.put("181", new TimezoneExt("181", "(GMT-04:00) Manaus",
				"America", "America/Manaus"));
		timeMap.put("182", new TimezoneExt("182", "(GMT-04:00) Marigot",
				"America", "America/Marigot"));
		timeMap.put("183", new TimezoneExt("183", "(GMT-04:00) Martinique",
				"America", "America/Martinique"));
		timeMap.put("184", new TimezoneExt("184", "(GMT-04:00) Moncton",
				"America", "America/Moncton"));
		timeMap.put("185", new TimezoneExt("185", "(GMT-04:00) Montserrat",
				"America", "America/Montserrat"));
		timeMap.put("186", new TimezoneExt("186", "(GMT-04:00) Port of Spain",
				"America", "America/Port_of_Spain"));
		timeMap.put("187", new TimezoneExt("187", "(GMT-04:00) Porto Acre",
				"America", "America/Porto_Acre"));
		timeMap.put("188", new TimezoneExt("188", "(GMT-04:00) Porto Velho",
				"America", "America/Porto_Velho"));
		timeMap.put("189", new TimezoneExt("189", "(GMT-04:00) Puerto Rico",
				"America", "America/Puerto_Rico"));
		timeMap.put("190", new TimezoneExt("190", "(GMT-04:00) Rio Branco",
				"America", "America/Rio_Branco"));
		timeMap.put("191", new TimezoneExt("191", "(GMT-04:00) Santiago",
				"America", "America/Santiago"));
		timeMap.put("192", new TimezoneExt("192", "(GMT-04:00) Santo Domingo",
				"America", "America/Santo_Domingo"));
		timeMap.put("193", new TimezoneExt("193", "(GMT-04:00) St Barthelemy",
				"America", "America/St_Barthelemy"));
		timeMap.put("194", new TimezoneExt("194", "(GMT-04:00) St Kitts",
				"America", "America/St_Kitts"));
		timeMap.put("195", new TimezoneExt("195", "(GMT-04:00) St Lucia",
				"America", "America/St_Lucia"));
		timeMap.put("196", new TimezoneExt("196", "(GMT-04:00) St Thomas",
				"America", "America/St_Thomas"));
		timeMap.put("197", new TimezoneExt("197", "(GMT-04:00) St Vincent",
				"America", "America/St_Vincent"));
		timeMap.put("198", new TimezoneExt("198", "(GMT-04:00) Thule",
				"America", "America/Thule"));
		timeMap.put("199", new TimezoneExt("199", "(GMT-04:00) Tortola",
				"America", "America/Tortola"));
		timeMap.put("200", new TimezoneExt("200", "(GMT-04:00) Virgin",
				"America", "America/Virgin"));
		timeMap.put("201", new TimezoneExt("201", "(GMT-04:00) Palmer",
				"Antarctica", "Antarctica/Palmer"));
		timeMap.put("202", new TimezoneExt("202", "(GMT-04:00) Bermuda",
				"Atlantic", "Atlantic/Bermuda"));
		timeMap.put("203", new TimezoneExt("203", "(GMT-04:00) Stanley",
				"Atlantic", "Atlantic/Stanley"));
		timeMap.put("212", new TimezoneExt("212", "(GMT-03:00) St Johns",
				"America", "America/St_Johns"));
		timeMap.put("216", new TimezoneExt("216", "(GMT-03:00) Araguaina",
				"America", "America/Araguaina"));
		timeMap.put("217", new TimezoneExt("217",
				"(GMT-03:00) Argentina/Buenos Aires", "America",
				"America/Argentina/Buenos_Aires"));
		timeMap.put("218", new TimezoneExt("218",
				"(GMT-03:00) Argentina/Catamarca", "America",
				"America/Argentina/Catamarca"));
		timeMap.put("219", new TimezoneExt("219",
				"(GMT-03:00) Argentina/ComodRivadavia", "America",
				"America/Argentina/ComodRivadavia"));
		timeMap.put("220", new TimezoneExt("220",
				"(GMT-03:00) Argentina/Cordoba", "America",
				"America/Argentina/Cordoba"));
		timeMap.put("221", new TimezoneExt("221",
				"(GMT-03:00) Argentina/Jujuy", "America",
				"America/Argentina/Jujuy"));
		timeMap.put("222", new TimezoneExt("222",
				"(GMT-03:00) Argentina/La Rioja", "America",
				"America/Argentina/La_Rioja"));
		timeMap.put("223", new TimezoneExt("223",
				"(GMT-03:00) Argentina/Mendoza", "America",
				"America/Argentina/Mendoza"));
		timeMap.put("224", new TimezoneExt("224",
				"(GMT-03:00) Argentina/Rio Gallegos", "America",
				"America/Argentina/Rio_Gallegos"));
		timeMap.put("225", new TimezoneExt("225",
				"(GMT-03:00) Argentina/Salta", "America",
				"America/Argentina/Salta"));
		timeMap.put("226", new TimezoneExt("226",
				"(GMT-03:00) Argentina/San Juan", "America",
				"America/Argentina/San_Juan"));
		timeMap.put("227", new TimezoneExt("227",
				"(GMT-03:00) Argentina/Tucuman", "America",
				"America/Argentina/Tucuman"));
		timeMap.put("228", new TimezoneExt("228",
				"(GMT-03:00) Argentina/Ushuaia", "America",
				"America/Argentina/Ushuaia"));
		timeMap.put("229", new TimezoneExt("229", "(GMT-03:00) Bahia",
				"America", "America/Bahia"));
		timeMap.put("230", new TimezoneExt("230", "(GMT-03:00) Belem",
				"America", "America/Belem"));
		timeMap.put("231", new TimezoneExt("231", "(GMT-03:00) Buenos Aires",
				"America", "America/Buenos_Aires"));
		timeMap.put("232", new TimezoneExt("232", "(GMT-03:00) Catamarca",
				"America", "America/Catamarca"));
		timeMap.put("233", new TimezoneExt("233", "(GMT-03:00) Cayenne",
				"America", "America/Cayenne"));
		timeMap.put("234", new TimezoneExt("234", "(GMT-03:00) Cordoba",
				"America", "America/Cordoba"));
		timeMap.put("235", new TimezoneExt("235", "(GMT-03:00) Fortaleza",
				"America", "America/Fortaleza"));
		timeMap.put("236", new TimezoneExt("236", "(GMT-03:00) Godthab",
				"America", "America/Godthab"));
		timeMap.put("237", new TimezoneExt("237", "(GMT-03:00) Jujuy",
				"America", "America/Jujuy"));
		timeMap.put("238", new TimezoneExt("238", "(GMT-03:00) Maceio",
				"America", "America/Maceio"));
		timeMap.put("239", new TimezoneExt("239", "(GMT-03:00) Mendoza",
				"America", "America/Mendoza"));
		timeMap.put("240", new TimezoneExt("240", "(GMT-03:00) Miquelon",
				"America", "America/Miquelon"));
		timeMap.put("241", new TimezoneExt("241", "(GMT-03:00) Montevideo",
				"America", "America/Montevideo"));
		timeMap.put("242", new TimezoneExt("242", "(GMT-03:00) Paramaribo",
				"America", "America/Paramaribo"));
		timeMap.put("243", new TimezoneExt("243", "(GMT-03:00) Recife",
				"America", "America/Recife"));
		timeMap.put("244", new TimezoneExt("244", "(GMT-03:00) Rosario",
				"America", "America/Rosario"));
		timeMap.put("245", new TimezoneExt("245", "(GMT-03:00) Santarem",
				"America", "America/Santarem"));
		timeMap.put("246", new TimezoneExt("246", "(GMT-03:00) Sao Paulo",
				"America", "America/Sao_Paulo"));
		timeMap.put("247", new TimezoneExt("247", "(GMT-03:00) Rothera",
				"Antarctica", "Antarctica/Rothera"));
		timeMap.put("251", new TimezoneExt("251", "(GMT-02:00) Noronha",
				"America", "America/Noronha"));
		timeMap.put("252", new TimezoneExt("252", "(GMT-02:00) South Georgia",
				"Atlantic", "Atlantic/South_Georgia"));
		timeMap.put("255", new TimezoneExt("255", "(GMT-01:00) Scoresbysund",
				"America", "America/Scoresbysund"));
		timeMap.put("256", new TimezoneExt("256", "(GMT-01:00) Azores",
				"Atlantic", "Atlantic/Azores"));
		timeMap.put("257", new TimezoneExt("257", "(GMT-01:00) Cape Verde",
				"Atlantic", "Atlantic/Cape_Verde"));
		timeMap.put("259", new TimezoneExt("259", "(GMT+00:00) Abidjan",
				"Africa", "Africa/Abidjan"));
		timeMap.put("260", new TimezoneExt("260", "(GMT+00:00) Accra",
				"Africa", "Africa/Accra"));
		timeMap.put("261", new TimezoneExt("261", "(GMT+00:00) Bamako",
				"Africa", "Africa/Bamako"));
		timeMap.put("262", new TimezoneExt("262", "(GMT+00:00) Banjul",
				"Africa", "Africa/Banjul"));
		timeMap.put("263", new TimezoneExt("263", "(GMT+00:00) Bissau",
				"Africa", "Africa/Bissau"));
		timeMap.put("264", new TimezoneExt("264", "(GMT+00:00) Casablanca",
				"Africa", "Africa/Casablanca"));
		timeMap.put("265", new TimezoneExt("265", "(GMT+00:00) Conakry",
				"Africa", "Africa/Conakry"));
		timeMap.put("266", new TimezoneExt("266", "(GMT+00:00) Dakar",
				"Africa", "Africa/Dakar"));
		timeMap.put("267", new TimezoneExt("267", "(GMT+00:00) El Aaiun",
				"Africa", "Africa/El_Aaiun"));
		timeMap.put("268", new TimezoneExt("268", "(GMT+00:00) Freetown",
				"Africa", "Africa/Freetown"));
		timeMap.put("269", new TimezoneExt("269", "(GMT+00:00) Lome", "Africa",
				"Africa/Lome"));
		timeMap.put("270", new TimezoneExt("270", "(GMT+00:00) Monrovia",
				"Africa", "Africa/Monrovia"));
		timeMap.put("271", new TimezoneExt("271", "(GMT+00:00) Nouakchott",
				"Africa", "Africa/Nouakchott"));
		timeMap.put("272", new TimezoneExt("272", "(GMT+00:00) Ouagadougou",
				"Africa", "Africa/Ouagadougou"));
		timeMap.put("273", new TimezoneExt("273", "(GMT+00:00) Sao Tome",
				"Africa", "Africa/Sao_Tome"));
		timeMap.put("274", new TimezoneExt("274", "(GMT+00:00) Timbuktu",
				"Africa", "Africa/Timbuktu"));
		timeMap.put("275", new TimezoneExt("275", "(GMT+00:00) Danmarkshavn",
				"America", "America/Danmarkshavn"));
		timeMap.put("276", new TimezoneExt("276", "(GMT+00:00) Canary",
				"Atlantic", "Atlantic/Canary"));
		timeMap.put("277", new TimezoneExt("277", "(GMT+00:00) Faeroe",
				"Atlantic", "Atlantic/Faeroe"));
		timeMap.put("278", new TimezoneExt("278", "(GMT+00:00) Faroe",
				"Atlantic", "Atlantic/Faroe"));
		timeMap.put("279", new TimezoneExt("279", "(GMT+00:00) Madeira",
				"Atlantic", "Atlantic/Madeira"));
		timeMap.put("280", new TimezoneExt("280", "(GMT+00:00) Reykjavik",
				"Atlantic", "Atlantic/Reykjavik"));
		timeMap.put("281", new TimezoneExt("281", "(GMT+00:00) St Helena",
				"Atlantic", "Atlantic/St_Helena"));
		timeMap.put("289", new TimezoneExt("289", "UTC", "UTC", "Etc/UTC"));
		timeMap.put("292", new TimezoneExt("292", "(GMT+00:00) Belfast",
				"Europe", "Europe/Belfast"));
		timeMap.put("293", new TimezoneExt("293", "(GMT+00:00) Dublin",
				"Europe", "Europe/Dublin"));
		timeMap.put("294", new TimezoneExt("294", "(GMT+00:00) Guernsey",
				"Europe", "Europe/Guernsey"));
		timeMap.put("295", new TimezoneExt("295", "(GMT+00:00) Isle of Man",
				"Europe", "Europe/Isle_of_Man"));
		timeMap.put("296", new TimezoneExt("296", "(GMT+00:00) Jersey",
				"Europe", "Europe/Jersey"));
		timeMap.put("297", new TimezoneExt("297", "(GMT+00:00) Lisbon",
				"Europe", "Europe/Lisbon"));
		timeMap.put("298", new TimezoneExt("298", "(GMT+00:00) London",
				"Europe", "Europe/London"));
		timeMap.put("311", new TimezoneExt("311", "(GMT+01:00) Algiers",
				"Africa", "Africa/Algiers"));
		timeMap.put("312", new TimezoneExt("312", "(GMT+01:00) Bangui",
				"Africa", "Africa/Bangui"));
		timeMap.put("313", new TimezoneExt("313", "(GMT+01:00) Brazzaville",
				"Africa", "Africa/Brazzaville"));
		timeMap.put("314", new TimezoneExt("314", "(GMT+01:00) Ceuta",
				"Africa", "Africa/Ceuta"));
		timeMap.put("315", new TimezoneExt("315", "(GMT+01:00) Douala",
				"Africa", "Africa/Douala"));
		timeMap.put("316", new TimezoneExt("316", "(GMT+01:00) Kinshasa",
				"Africa", "Africa/Kinshasa"));
		timeMap.put("317", new TimezoneExt("317", "(GMT+01:00) Lagos",
				"Africa", "Africa/Lagos"));
		timeMap.put("318", new TimezoneExt("318", "(GMT+01:00) Libreville",
				"Africa", "Africa/Libreville"));
		timeMap.put("319", new TimezoneExt("319", "(GMT+01:00) Luanda",
				"Africa", "Africa/Luanda"));
		timeMap.put("320", new TimezoneExt("320", "(GMT+01:00) Malabo",
				"Africa", "Africa/Malabo"));
		timeMap.put("321", new TimezoneExt("321", "(GMT+01:00) Ndjamena",
				"Africa", "Africa/Ndjamena"));
		timeMap.put("322", new TimezoneExt("322", "(GMT+01:00) Niamey",
				"Africa", "Africa/Niamey"));
		timeMap.put("323", new TimezoneExt("323", "(GMT+01:00) Porto-Novo",
				"Africa", "Africa/Porto-Novo"));
		timeMap.put("324", new TimezoneExt("324", "(GMT+01:00) Tunis",
				"Africa", "Africa/Tunis"));
		timeMap.put("325", new TimezoneExt("325", "(GMT+01:00) Windhoek",
				"Africa", "Africa/Windhoek"));
		timeMap.put("327", new TimezoneExt("327", "(GMT+01:00) Jan Mayen",
				"Atlantic", "Atlantic/Jan_Mayen"));
		timeMap.put("331", new TimezoneExt("331", "(GMT+01:00) Amsterdam",
				"Europe", "Europe/Amsterdam"));
		timeMap.put("332", new TimezoneExt("332", "(GMT+01:00) Andorra",
				"Europe", "Europe/Andorra"));
		timeMap.put("333", new TimezoneExt("333", "(GMT+01:00) Belgrade",
				"Europe", "Europe/Belgrade"));
		timeMap.put("334", new TimezoneExt("334", "(GMT+01:00) Berlin",
				"Europe", "Europe/Berlin"));
		timeMap.put("335", new TimezoneExt("335", "(GMT+01:00) Bratislava",
				"Europe", "Europe/Bratislava"));
		timeMap.put("336", new TimezoneExt("336", "(GMT+01:00) Brussels",
				"Europe", "Europe/Brussels"));
		timeMap.put("337", new TimezoneExt("337", "(GMT+01:00) Budapest",
				"Europe", "Europe/Budapest"));
		timeMap.put("338", new TimezoneExt("338", "(GMT+01:00) Copenhagen",
				"Europe", "Europe/Copenhagen"));
		timeMap.put("339", new TimezoneExt("339", "(GMT+01:00) Gibraltar",
				"Europe", "Europe/Gibraltar"));
		timeMap.put("340", new TimezoneExt("340", "(GMT+01:00) Ljubljana",
				"Europe", "Europe/Ljubljana"));
		timeMap.put("341", new TimezoneExt("341", "(GMT+01:00) Luxembourg",
				"Europe", "Europe/Luxembourg"));
		timeMap.put("342", new TimezoneExt("342", "(GMT+01:00) Madrid",
				"Europe", "Europe/Madrid"));
		timeMap.put("343", new TimezoneExt("343", "(GMT+01:00) Malta",
				"Europe", "Europe/Malta"));
		timeMap.put("344", new TimezoneExt("344", "(GMT+01:00) Monaco",
				"Europe", "Europe/Monaco"));
		timeMap.put("345", new TimezoneExt("345", "(GMT+01:00) Oslo", "Europe",
				"Europe/Oslo"));
		timeMap.put("346", new TimezoneExt("346", "(GMT+01:00) Paris",
				"Europe", "Europe/Paris"));
		timeMap.put("347", new TimezoneExt("347", "(GMT+01:00) Podgorica",
				"Europe", "Europe/Podgorica"));
		timeMap.put("348", new TimezoneExt("348", "(GMT+01:00) Prague",
				"Europe", "Europe/Prague"));
		timeMap.put("349", new TimezoneExt("349", "(GMT+01:00) Rome", "Europe",
				"Europe/Rome"));
		timeMap.put("350", new TimezoneExt("350", "(GMT+01:00) San Marino",
				"Europe", "Europe/San_Marino"));
		timeMap.put("351", new TimezoneExt("351", "(GMT+01:00) Sarajevo",
				"Europe", "Europe/Sarajevo"));
		timeMap.put("352", new TimezoneExt("352", "(GMT+01:00) Skopje",
				"Europe", "Europe/Skopje"));
		timeMap.put("353", new TimezoneExt("353", "(GMT+01:00) Stockholm",
				"Europe", "Europe/Stockholm"));
		timeMap.put("354", new TimezoneExt("354", "(GMT+01:00) Tirane",
				"Europe", "Europe/Tirane"));
		timeMap.put("355", new TimezoneExt("355", "(GMT+01:00) Vaduz",
				"Europe", "Europe/Vaduz"));
		timeMap.put("356", new TimezoneExt("356", "(GMT+01:00) Vatican",
				"Europe", "Europe/Vatican"));
		timeMap.put("357", new TimezoneExt("357", "(GMT+01:00) Vienna",
				"Europe", "Europe/Vienna"));
		timeMap.put("358", new TimezoneExt("358", "(GMT+01:00) Warsaw",
				"Europe", "Europe/Warsaw"));
		timeMap.put("359", new TimezoneExt("359", "(GMT+01:00) Zagreb",
				"Europe", "Europe/Zagreb"));
		timeMap.put("360", new TimezoneExt("360", "(GMT+01:00) Zurich",
				"Europe", "Europe/Zurich"));
		timeMap.put("364", new TimezoneExt("364", "(GMT+02:00) Blantyre",
				"Africa", "Africa/Blantyre"));
		timeMap.put("365", new TimezoneExt("365", "(GMT+02:00) Bujumbura",
				"Africa", "Africa/Bujumbura"));
		timeMap.put("366", new TimezoneExt("366", "(GMT+02:00) Cairo",
				"Africa", "Africa/Cairo"));
		timeMap.put("367", new TimezoneExt("367", "(GMT+02:00) Gaborone",
				"Africa", "Africa/Gaborone"));
		timeMap.put("368", new TimezoneExt("368", "(GMT+02:00) Harare",
				"Africa", "Africa/Harare"));
		timeMap.put("369", new TimezoneExt("369", "(GMT+02:00) Johannesburg",
				"Africa", "Africa/Johannesburg"));
		timeMap.put("370", new TimezoneExt("370", "(GMT+02:00) Kigali",
				"Africa", "Africa/Kigali"));
		timeMap.put("371", new TimezoneExt("371", "(GMT+02:00) Lubumbashi",
				"Africa", "Africa/Lubumbashi"));
		timeMap.put("372", new TimezoneExt("372", "(GMT+02:00) Lusaka",
				"Africa", "Africa/Lusaka"));
		timeMap.put("373", new TimezoneExt("373", "(GMT+02:00) Maputo",
				"Africa", "Africa/Maputo"));
		timeMap.put("374", new TimezoneExt("374", "(GMT+02:00) Maseru",
				"Africa", "Africa/Maseru"));
		timeMap.put("375", new TimezoneExt("375", "(GMT+02:00) Mbabane",
				"Africa", "Africa/Mbabane"));
		timeMap.put("376", new TimezoneExt("376", "(GMT+02:00) Tripoli",
				"Africa", "Africa/Tripoli"));
		timeMap.put("377", new TimezoneExt("377", "(GMT+02:00) Amman", "Asia",
				"Asia/Amman"));
		timeMap.put("378", new TimezoneExt("378", "(GMT+02:00) Beirut", "Asia",
				"Asia/Beirut"));
		timeMap.put("379", new TimezoneExt("379", "(GMT+02:00) Damascus",
				"Asia", "Asia/Damascus"));
		timeMap.put("380", new TimezoneExt("380", "(GMT+02:00) Gaza", "Asia",
				"Asia/Gaza"));
		timeMap.put("381", new TimezoneExt("381", "(GMT+02:00) Hebron", "Asia",
				"Asia/Hebron"));
		timeMap.put("382", new TimezoneExt("382", "(GMT+02:00) Istanbul",
				"Asia", "Asia/Istanbul"));
		timeMap.put("383", new TimezoneExt("383", "(GMT+02:00) Jerusalem",
				"Asia", "Asia/Jerusalem"));
		timeMap.put("384", new TimezoneExt("384", "(GMT+02:00) Nicosia",
				"Asia", "Asia/Nicosia"));
		timeMap.put("385", new TimezoneExt("385", "(GMT+02:00) Tel Aviv",
				"Asia", "Asia/Tel_Aviv"));
		timeMap.put("390", new TimezoneExt("390", "(GMT+02:00) Athens",
				"Europe", "Europe/Athens"));
		timeMap.put("391", new TimezoneExt("391", "(GMT+02:00) Bucharest",
				"Europe", "Europe/Bucharest"));
		timeMap.put("392", new TimezoneExt("392", "(GMT+02:00) Chisinau",
				"Europe", "Europe/Chisinau"));
		timeMap.put("393", new TimezoneExt("393", "(GMT+02:00) Helsinki",
				"Europe", "Europe/Helsinki"));
		timeMap.put("394", new TimezoneExt("394", "(GMT+02:00) Istanbul",
				"Europe", "Europe/Istanbul"));
		timeMap.put("395", new TimezoneExt("395", "(GMT+02:00) Mariehamn",
				"Europe", "Europe/Mariehamn"));
		timeMap.put("396", new TimezoneExt("396", "(GMT+02:00) Nicosia",
				"Europe", "Europe/Nicosia"));
		timeMap.put("397", new TimezoneExt("397", "(GMT+02:00) Riga", "Europe",
				"Europe/Riga"));
		timeMap.put("398", new TimezoneExt("398", "(GMT+02:00) Sofia",
				"Europe", "Europe/Sofia"));
		timeMap.put("399", new TimezoneExt("399", "(GMT+02:00) Tallinn",
				"Europe", "Europe/Tallinn"));
		timeMap.put("400", new TimezoneExt("400", "(GMT+02:00) Tiraspol",
				"Europe", "Europe/Tiraspol"));
		timeMap.put("401", new TimezoneExt("401", "(GMT+02:00) Vilnius",
				"Europe", "Europe/Vilnius"));
		timeMap.put("405", new TimezoneExt("405", "(GMT+03:00) Addis Ababa",
				"Africa", "Africa/Addis_Ababa"));
		timeMap.put("406", new TimezoneExt("406", "(GMT+03:00) Asmara",
				"Africa", "Africa/Asmara"));
		timeMap.put("407", new TimezoneExt("407", "(GMT+03:00) Asmera",
				"Africa", "Africa/Asmera"));
		timeMap.put("408", new TimezoneExt("408", "(GMT+03:00) Dar es Salaam",
				"Africa", "Africa/Dar_es_Salaam"));
		timeMap.put("409", new TimezoneExt("409", "(GMT+03:00) Djibouti",
				"Africa", "Africa/Djibouti"));
		timeMap.put("410", new TimezoneExt("410", "(GMT+03:00) Juba", "Africa",
				"Africa/Juba"));
		timeMap.put("411", new TimezoneExt("411", "(GMT+03:00) Kampala",
				"Africa", "Africa/Kampala"));
		timeMap.put("412", new TimezoneExt("412", "(GMT+03:00) Khartoum",
				"Africa", "Africa/Khartoum"));
		timeMap.put("413", new TimezoneExt("413", "(GMT+03:00) Mogadishu",
				"Africa", "Africa/Mogadishu"));
		timeMap.put("414", new TimezoneExt("414", "(GMT+03:00) Nairobi",
				"Africa", "Africa/Nairobi"));
		timeMap.put("415", new TimezoneExt("415", "(GMT+03:00) Syowa",
				"Antarctica", "Antarctica/Syowa"));
		timeMap.put("416", new TimezoneExt("416", "(GMT+03:00) Aden", "Asia",
				"Asia/Aden"));
		timeMap.put("417", new TimezoneExt("417", "(GMT+03:00) Baghdad",
				"Asia", "Asia/Baghdad"));
		timeMap.put("418", new TimezoneExt("418", "(GMT+03:00) Bahrain",
				"Asia", "Asia/Bahrain"));
		timeMap.put("419", new TimezoneExt("419", "(GMT+03:00) Kuwait", "Asia",
				"Asia/Kuwait"));
		timeMap.put("420", new TimezoneExt("420", "(GMT+03:00) Qatar", "Asia",
				"Asia/Qatar"));
		timeMap.put("421", new TimezoneExt("421", "(GMT+03:00) Riyadh", "Asia",
				"Asia/Riyadh"));
		timeMap.put("424", new TimezoneExt("424", "(GMT+03:00) Kaliningrad",
				"Europe", "Europe/Kaliningrad"));
		timeMap.put("425", new TimezoneExt("425", "(GMT+03:00) Kiev", "Europe",
				"Europe/Kiev"));
		timeMap.put("426", new TimezoneExt("426", "(GMT+03:00) Minsk",
				"Europe", "Europe/Minsk"));
		timeMap.put("427", new TimezoneExt("427", "(GMT+03:00) Simferopol",
				"Europe", "Europe/Simferopol"));
		timeMap.put("428", new TimezoneExt("428", "(GMT+03:00) Uzhgorod",
				"Europe", "Europe/Uzhgorod"));
		timeMap.put("429", new TimezoneExt("429", "(GMT+03:00) Zaporozhye",
				"Europe", "Europe/Zaporozhye"));
		timeMap.put("430", new TimezoneExt("430", "(GMT+03:00) Antananarivo",
				"Indian", "Indian/Antananarivo"));
		timeMap.put("431", new TimezoneExt("431", "(GMT+03:00) Comoro",
				"Indian", "Indian/Comoro"));
		timeMap.put("432", new TimezoneExt("432", "(GMT+03:00) Mayotte",
				"Indian", "Indian/Mayotte"));
		timeMap.put("433", new TimezoneExt("433", "(GMT+03:00) Riyadh87",
				"Asia", "Asia/Riyadh87"));
		timeMap.put("434", new TimezoneExt("434", "(GMT+03:00) Riyadh88",
				"Asia", "Asia/Riyadh88"));
		timeMap.put("435", new TimezoneExt("435", "(GMT+03:00) Riyadh89",
				"Asia", "Asia/Riyadh89"));
		timeMap.put("439", new TimezoneExt("439", "(GMT+03:00) Tehran", "Asia",
				"Asia/Tehran"));
		timeMap.put("441", new TimezoneExt("441", "(GMT+04:00) Baku", "Asia",
				"Asia/Baku"));
		timeMap.put("442", new TimezoneExt("442", "(GMT+04:00) Dubai", "Asia",
				"Asia/Dubai"));
		timeMap.put("443", new TimezoneExt("443", "(GMT+04:00) Muscat", "Asia",
				"Asia/Muscat"));
		timeMap.put("444", new TimezoneExt("444", "(GMT+04:00) Tbilisi",
				"Asia", "Asia/Tbilisi"));
		timeMap.put("445", new TimezoneExt("445", "(GMT+04:00) Yerevan",
				"Asia", "Asia/Yerevan"));
		timeMap.put("447", new TimezoneExt("447", "(GMT+04:00) Moscow",
				"Europe", "Europe/Moscow"));
		timeMap.put("448", new TimezoneExt("448", "(GMT+04:00) Samara",
				"Europe", "Europe/Samara"));
		timeMap.put("449", new TimezoneExt("449", "(GMT+04:00) Volgograd",
				"Europe", "Europe/Volgograd"));
		timeMap.put("450", new TimezoneExt("450", "(GMT+04:00) Mahe", "Indian",
				"Indian/Mahe"));
		timeMap.put("451", new TimezoneExt("451", "(GMT+04:00) Mauritius",
				"Indian", "Indian/Mauritius"));
		timeMap.put("452", new TimezoneExt("452", "(GMT+04:00) Reunion",
				"Indian", "Indian/Reunion"));
		timeMap.put("455", new TimezoneExt("455", "(GMT+04:00) Kabul", "Asia",
				"Asia/Kabul"));
		timeMap.put("456", new TimezoneExt("456", "(GMT+05:00) Mawson",
				"Antarctica", "Antarctica/Mawson"));
		timeMap.put("457", new TimezoneExt("457", "(GMT+05:00) Aqtau", "Asia",
				"Asia/Aqtau"));
		timeMap.put("458", new TimezoneExt("458", "(GMT+05:00) Aqtobe", "Asia",
				"Asia/Aqtobe"));
		timeMap.put("459", new TimezoneExt("459", "(GMT+05:00) Ashgabat",
				"Asia", "Asia/Ashgabat"));
		timeMap.put("460", new TimezoneExt("460", "(GMT+05:00) Ashkhabad",
				"Asia", "Asia/Ashkhabad"));
		timeMap.put("461", new TimezoneExt("461", "(GMT+05:00) Dushanbe",
				"Asia", "Asia/Dushanbe"));
		timeMap.put("462", new TimezoneExt("462", "(GMT+05:00) Karachi",
				"Asia", "Asia/Karachi"));
		timeMap.put("463", new TimezoneExt("463", "(GMT+05:00) Oral", "Asia",
				"Asia/Oral"));
		timeMap.put("464", new TimezoneExt("464", "(GMT+05:00) Samarkand",
				"Asia", "Asia/Samarkand"));
		timeMap.put("465", new TimezoneExt("465", "(GMT+05:00) Tashkent",
				"Asia", "Asia/Tashkent"));
		timeMap.put("467", new TimezoneExt("467", "(GMT+05:00) Kerguelen",
				"Indian", "Indian/Kerguelen"));
		timeMap.put("468", new TimezoneExt("468", "(GMT+05:00) Maldives",
				"Indian", "Indian/Maldives"));
		timeMap.put("470", new TimezoneExt("470", "(GMT+05:00) Calcutta",
				"Asia", "Asia/Calcutta"));
		timeMap.put("471", new TimezoneExt("471", "(GMT+05:00) Colombo",
				"Asia", "Asia/Colombo"));
		timeMap.put("472", new TimezoneExt("472", "(GMT+05:00) Kolkata",
				"Asia", "Asia/Kolkata"));
		timeMap.put("474", new TimezoneExt("474", "(GMT+05:00) Kathmandu",
				"Asia", "Asia/Kathmandu"));
		timeMap.put("475", new TimezoneExt("475", "(GMT+05:00) Katmandu",
				"Asia", "Asia/Katmandu"));
		timeMap.put("476", new TimezoneExt("476", "(GMT+06:00) Vostok",
				"Antarctica", "Antarctica/Vostok"));
		timeMap.put("477", new TimezoneExt("477", "(GMT+06:00) Almaty", "Asia",
				"Asia/Almaty"));
		timeMap.put("478", new TimezoneExt("478", "(GMT+06:00) Bishkek",
				"Asia", "Asia/Bishkek"));
		timeMap.put("479", new TimezoneExt("479", "(GMT+06:00) Dacca", "Asia",
				"Asia/Dacca"));
		timeMap.put("480", new TimezoneExt("480", "(GMT+06:00) Dhaka", "Asia",
				"Asia/Dhaka"));
		timeMap.put("481", new TimezoneExt("481", "(GMT+06:00) Qyzylorda",
				"Asia", "Asia/Qyzylorda"));
		timeMap.put("482", new TimezoneExt("482", "(GMT+06:00) Thimbu", "Asia",
				"Asia/Thimbu"));
		timeMap.put("483", new TimezoneExt("483", "(GMT+06:00) Thimphu",
				"Asia", "Asia/Thimphu"));
		timeMap.put("484", new TimezoneExt("484", "(GMT+06:00) Yekaterinburg",
				"Asia", "Asia/Yekaterinburg"));
		timeMap.put("487", new TimezoneExt("487", "(GMT+06:00) Chagos",
				"Indian", "Indian/Chagos"));
		timeMap.put("488", new TimezoneExt("488", "(GMT+06:00) Rangoon",
				"Asia", "Asia/Rangoon"));
		timeMap.put("489", new TimezoneExt("489", "(GMT+06:00) Cocos",
				"Indian", "Indian/Cocos"));
		timeMap.put("490", new TimezoneExt("490", "(GMT+07:00) Davis",
				"Antarctica", "Antarctica/Davis"));
		timeMap.put("491", new TimezoneExt("491", "(GMT+07:00) Bangkok",
				"Asia", "Asia/Bangkok"));
		timeMap.put("492", new TimezoneExt("492", "(GMT+07:00) Ho Chi Minh",
				"Asia", "Asia/Ho_Chi_Minh"));
		timeMap.put("493", new TimezoneExt("493", "(GMT+07:00) Hovd", "Asia",
				"Asia/Hovd"));
		timeMap.put("494", new TimezoneExt("494", "(GMT+07:00) Jakarta",
				"Asia", "Asia/Jakarta"));
		timeMap.put("495", new TimezoneExt("495", "(GMT+07:00) Novokuznetsk",
				"Asia", "Asia/Novokuznetsk"));
		timeMap.put("496", new TimezoneExt("496", "(GMT+07:00) Novosibirsk",
				"Asia", "Asia/Novosibirsk"));
		timeMap.put("497", new TimezoneExt("497", "(GMT+07:00) Omsk", "Asia",
				"Asia/Omsk"));
		timeMap.put("498", new TimezoneExt("498", "(GMT+07:00) Phnom Penh",
				"Asia", "Asia/Phnom_Penh"));
		timeMap.put("499", new TimezoneExt("499", "(GMT+07:00) Pontianak",
				"Asia", "Asia/Pontianak"));
		timeMap.put("500", new TimezoneExt("500", "(GMT+07:00) Saigon", "Asia",
				"Asia/Saigon"));
		timeMap.put("501", new TimezoneExt("501", "(GMT+07:00) Vientiane",
				"Asia", "Asia/Vientiane"));
		timeMap.put("503", new TimezoneExt("503", "(GMT+07:00) Christmas",
				"Indian", "Indian/Christmas"));
		timeMap.put("505", new TimezoneExt("505", "(GMT+08:00) Casey",
				"Antarctica", "Antarctica/Casey"));
		timeMap.put("506", new TimezoneExt("506", "(GMT+08:00) Brunei", "Asia",
				"Asia/Brunei"));
		timeMap.put("507", new TimezoneExt("507", "(GMT+08:00) Choibalsan",
				"Asia", "Asia/Choibalsan"));
		timeMap.put("508", new TimezoneExt("508", "(GMT+08:00) Chongqing",
				"Asia", "Asia/Chongqing"));
		timeMap.put("509", new TimezoneExt("509", "(GMT+08:00) Chungking",
				"Asia", "Asia/Chungking"));
		timeMap.put("510", new TimezoneExt("510", "(GMT+08:00) Harbin", "Asia",
				"Asia/Harbin"));
		timeMap.put("511", new TimezoneExt("511", "(GMT+08:00) Hong Kong",
				"Asia", "Asia/Hong_Kong"));
		timeMap.put("512", new TimezoneExt("512", "(GMT+08:00) Kashgar",
				"Asia", "Asia/Kashgar"));
		timeMap.put("513", new TimezoneExt("513", "(GMT+08:00) Krasnoyarsk",
				"Asia", "Asia/Krasnoyarsk"));
		timeMap.put("514", new TimezoneExt("514", "(GMT+08:00) Kuala Lumpur",
				"Asia", "Asia/Kuala_Lumpur"));
		timeMap.put("515", new TimezoneExt("515", "(GMT+08:00) Kuching",
				"Asia", "Asia/Kuching"));
		timeMap.put("516", new TimezoneExt("516", "(GMT+08:00) Macao", "Asia",
				"Asia/Macao"));
		timeMap.put("517", new TimezoneExt("517", "(GMT+08:00) Macau", "Asia",
				"Asia/Macau"));
		timeMap.put("518", new TimezoneExt("518", "(GMT+08:00) Makassar",
				"Asia", "Asia/Makassar"));
		timeMap.put("519", new TimezoneExt("519", "(GMT+08:00) Manila", "Asia",
				"Asia/Manila"));
		timeMap.put("520", new TimezoneExt("520", "(GMT+08:00) Shanghai",
				"Asia", "Asia/Shanghai"));
		timeMap.put("521", new TimezoneExt("521", "(GMT+08:00) Singapore",
				"Asia", "Asia/Singapore"));
		timeMap.put("522", new TimezoneExt("522", "(GMT+08:00) Taipei", "Asia",
				"Asia/Taipei"));
		timeMap.put("523", new TimezoneExt("523", "(GMT+08:00) Ujung Pandang",
				"Asia", "Asia/Ujung_Pandang"));
		timeMap.put("524", new TimezoneExt("524", "(GMT+08:00) Ulaanbaatar",
				"Asia", "Asia/Ulaanbaatar"));
		timeMap.put("525", new TimezoneExt("525", "(GMT+08:00) Ulan Bator",
				"Asia", "Asia/Ulan_Bator"));
		timeMap.put("526", new TimezoneExt("526", "(GMT+08:00) Urumqi", "Asia",
				"Asia/Urumqi"));
		timeMap.put("527", new TimezoneExt("527", "(GMT+08:00) Perth",
				"Australia", "Australia/Perth"));
		timeMap.put("528", new TimezoneExt("528", "(GMT+08:00) West",
				"Australia", "Australia/West"));
		timeMap.put("534", new TimezoneExt("534", "(GMT+08:00) Eucla",
				"Australia", "Australia/Eucla"));
		timeMap.put("535", new TimezoneExt("535", "(GMT+09:00) Dili", "Asia",
				"Asia/Dili"));
		timeMap.put("536", new TimezoneExt("536", "(GMT+09:00) Irkutsk",
				"Asia", "Asia/Irkutsk"));
		timeMap.put("537", new TimezoneExt("537", "(GMT+09:00) Jayapura",
				"Asia", "Asia/Jayapura"));
		timeMap.put("538", new TimezoneExt("538", "(GMT+09:00) Pyongyang",
				"Asia", "Asia/Pyongyang"));
		timeMap.put("539", new TimezoneExt("539", "(GMT+09:00) Seoul", "Asia",
				"Asia/Seoul"));
		timeMap.put("540", new TimezoneExt("540", "(GMT+09:00) Tokyo", "Asia",
				"Asia/Tokyo"));
		timeMap.put("544", new TimezoneExt("544", "(GMT+09:00) Palau",
				"Pacific", "Pacific/Palau"));
		timeMap.put("547", new TimezoneExt("547", "(GMT+09:00) Adelaide",
				"Australia", "Australia/Adelaide"));
		timeMap.put("548", new TimezoneExt("548", "(GMT+09:00) Broken Hill",
				"Australia", "Australia/Broken_Hill"));
		timeMap.put("549", new TimezoneExt("549", "(GMT+09:00) Darwin",
				"Australia", "Australia/Darwin"));
		timeMap.put("550", new TimezoneExt("550", "(GMT+09:00) North",
				"Australia", "Australia/North"));
		timeMap.put("551", new TimezoneExt("551", "(GMT+09:00) South",
				"Australia", "Australia/South"));
		timeMap.put("552", new TimezoneExt("552", "(GMT+09:00) Yancowinna",
				"Australia", "Australia/Yancowinna"));
		timeMap.put("554", new TimezoneExt("554", "(GMT+10:00) DumontDUrville",
				"Antarctica", "Antarctica/DumontDUrville"));
		timeMap.put("555", new TimezoneExt("555", "(GMT+10:00) Yakutsk",
				"Asia", "Asia/Yakutsk"));
		timeMap.put("556", new TimezoneExt("556", "(GMT+10:00) ACT",
				"Australia", "Australia/ACT"));
		timeMap.put("557", new TimezoneExt("557", "(GMT+10:00) Brisbane",
				"Australia", "Australia/Brisbane"));
		timeMap.put("558", new TimezoneExt("558", "(GMT+10:00) Canberra",
				"Australia", "Australia/Canberra"));
		timeMap.put("559", new TimezoneExt("559", "(GMT+10:00) Currie",
				"Australia", "Australia/Currie"));
		timeMap.put("560", new TimezoneExt("560", "(GMT+10:00) Hobart",
				"Australia", "Australia/Hobart"));
		timeMap.put("561", new TimezoneExt("561", "(GMT+10:00) Lindeman",
				"Australia", "Australia/Lindeman"));
		timeMap.put("562", new TimezoneExt("562", "(GMT+10:00) Melbourne",
				"Australia", "Australia/Melbourne"));
		timeMap.put("563", new TimezoneExt("563", "(GMT+10:00) NSW",
				"Australia", "Australia/NSW"));
		timeMap.put("564", new TimezoneExt("564", "(GMT+10:00) Queensland",
				"Australia", "Australia/Queensland"));
		timeMap.put("565", new TimezoneExt("565", "(GMT+10:00) Sydney",
				"Australia", "Australia/Sydney"));
		timeMap.put("566", new TimezoneExt("566", "(GMT+10:00) Tasmania",
				"Australia", "Australia/Tasmania"));
		timeMap.put("567", new TimezoneExt("567", "(GMT+10:00) Victoria",
				"Australia", "Australia/Victoria"));
		timeMap.put("569", new TimezoneExt("569", "(GMT+10:00) Chuuk",
				"Pacific", "Pacific/Chuuk"));
		timeMap.put("570", new TimezoneExt("570", "(GMT+10:00) Guam",
				"Pacific", "Pacific/Guam"));
		timeMap.put("571", new TimezoneExt("571", "(GMT+10:00) Port Moresby",
				"Pacific", "Pacific/Port_Moresby"));
		timeMap.put("572", new TimezoneExt("572", "(GMT+10:00) Saipan",
				"Pacific", "Pacific/Saipan"));
		timeMap.put("573", new TimezoneExt("573", "(GMT+10:00) Truk",
				"Pacific", "Pacific/Truk"));
		timeMap.put("574", new TimezoneExt("574", "(GMT+10:00) Yap", "Pacific",
				"Pacific/Yap"));
		timeMap.put("575", new TimezoneExt("575", "(GMT+10:00) LHI",
				"Australia", "Australia/LHI"));
		timeMap.put("576", new TimezoneExt("576", "(GMT+10:00) Lord Howe",
				"Australia", "Australia/Lord_Howe"));
		timeMap.put("577", new TimezoneExt("577", "(GMT+11:00) Macquarie",
				"Antarctica", "Antarctica/Macquarie"));
		timeMap.put("578", new TimezoneExt("578", "(GMT+11:00) Sakhalin",
				"Asia", "Asia/Sakhalin"));
		timeMap.put("579", new TimezoneExt("579", "(GMT+11:00) Vladivostok",
				"Asia", "Asia/Vladivostok"));
		timeMap.put("581", new TimezoneExt("581", "(GMT+11:00) Efate",
				"Pacific", "Pacific/Efate"));
		timeMap.put("582", new TimezoneExt("582", "(GMT+11:00) Guadalcanal",
				"Pacific", "Pacific/Guadalcanal"));
		timeMap.put("583", new TimezoneExt("583", "(GMT+11:00) Kosrae",
				"Pacific", "Pacific/Kosrae"));
		timeMap.put("584", new TimezoneExt("584", "(GMT+11:00) Noumea",
				"Pacific", "Pacific/Noumea"));
		timeMap.put("585", new TimezoneExt("585", "(GMT+11:00) Pohnpei",
				"Pacific", "Pacific/Pohnpei"));
		timeMap.put("586", new TimezoneExt("586", "(GMT+11:00) Ponape",
				"Pacific", "Pacific/Ponape"));
		timeMap.put("588", new TimezoneExt("588", "(GMT+11:00) Norfolk",
				"Pacific", "Pacific/Norfolk"));
		timeMap.put("589", new TimezoneExt("589", "(GMT+12:00) McMurdo",
				"Antarctica", "Antarctica/McMurdo"));
		timeMap.put("590", new TimezoneExt("590", "(GMT+12:00) South Pole",
				"Antarctica", "Antarctica/South_Pole"));
		timeMap.put("591", new TimezoneExt("591", "(GMT+12:00) Anadyr", "Asia",
				"Asia/Anadyr"));
		timeMap.put("592", new TimezoneExt("592", "(GMT+12:00) Kamchatka",
				"Asia", "Asia/Kamchatka"));
		timeMap.put("593", new TimezoneExt("593", "(GMT+12:00) Magadan",
				"Asia", "Asia/Magadan"));
		timeMap.put("598", new TimezoneExt("598", "(GMT+12:00) Auckland",
				"Pacific", "Pacific/Auckland"));
		timeMap.put("599", new TimezoneExt("599", "(GMT+12:00) Fiji",
				"Pacific", "Pacific/Fiji"));
		timeMap.put("600", new TimezoneExt("600", "(GMT+12:00) Funafuti",
				"Pacific", "Pacific/Funafuti"));
		timeMap.put("601", new TimezoneExt("601", "(GMT+12:00) Kwajalein",
				"Pacific", "Pacific/Kwajalein"));
		timeMap.put("602", new TimezoneExt("602", "(GMT+12:00) Majuro",
				"Pacific", "Pacific/Majuro"));
		timeMap.put("603", new TimezoneExt("603", "(GMT+12:00) Nauru",
				"Pacific", "Pacific/Nauru"));
		timeMap.put("604", new TimezoneExt("604", "(GMT+12:00) Tarawa",
				"Pacific", "Pacific/Tarawa"));
		timeMap.put("605", new TimezoneExt("605", "(GMT+12:00) Wake",
				"Pacific", "Pacific/Wake"));
		timeMap.put("606", new TimezoneExt("606", "(GMT+12:00) Wallis",
				"Pacific", "Pacific/Wallis"));
		timeMap.put("608", new TimezoneExt("608", "(GMT+12:00) Chatham",
				"Pacific", "Pacific/Chatham"));
		timeMap.put("611", new TimezoneExt("611", "(GMT+13:00) Apia",
				"Pacific", "Pacific/Apia"));
		timeMap.put("612", new TimezoneExt("612", "(GMT+13:00) Enderbury",
				"Pacific", "Pacific/Enderbury"));
		timeMap.put("613", new TimezoneExt("613", "(GMT+13:00) Tongatapu",
				"Pacific", "Pacific/Tongatapu"));
		timeMap.put("615", new TimezoneExt("615", "(GMT+14:00) Kiritimati",
				"Pacific", "Pacific/Kiritimati"));
	}

	public static TimezoneExt getTimezoneExt(String mycollabId) {
		if (mycollabId == null || mycollabId.equals("")) {
			return timeMap.get("3");
		}
		return timeMap.get(mycollabId);
	}

	public static TimeZone getTimezone(String mycollabId) {
		TimezoneExt timeZoneExt = null;
		if (mycollabId == null || mycollabId.equals("")) {
			timeZoneExt = timeMap.get("3");
		} else {
			timeZoneExt = timeMap.get(mycollabId);
		}

		if (timeZoneExt == null) {
			throw new MyCollabException("Can not find the timezone with id "
					+ mycollabId);
		}

		return timeZoneExt.getTimezone();
	}

	public static String getTimezoneDbId(TimeZone timeZone) {
		String timeZoneId = timeZone.getID();
		for (TimezoneExt timeZoneExt : timeMap.values()) {
			if (timeZoneExt.getTimezone().getID().equals(timeZoneId)) {
				return timeZoneExt.getId();
			}
		}

		return "3";
	}

	public static class TimezoneExt {
		private final String id;
		private final String displayName;
		private final TimeZone timezone;
		private final String area;

		TimezoneExt(String id, String displayName, String area,
				String javaTimeZoneId) {
			this.id = id;
			this.displayName = displayName;
			this.area = area;
			this.timezone = TimeZone.getTimeZone(javaTimeZoneId);
		}

		public String getDisplayName() {
			return displayName;
		}

		public TimeZone getTimezone() {
			return timezone;
		}

		public String getArea() {
			return area;
		}

		public String getId() {
			return id;
		}
	}

	private static String getArea(String timeZoneDisplay) {
		String areas[] = new String[] { "Africa", "America", "Antarctica",
				"Asia", "Atlantic", "Australia", "Europe", "Etc", "Indian",
				"Pacific" };
		if (timeZoneDisplay.indexOf("/") > -1) {
			String area = timeZoneDisplay.substring(0,
					timeZoneDisplay.indexOf("/"));
			for (String item : areas) {
				if (item.equalsIgnoreCase(area.trim())) {
					return (area.equalsIgnoreCase("etc")) ? "GMT Offset" : item;
				}
			}
		}
		return "";
	}

	private static String getOffsetString(TimeZone timeZone) {
		int offset = timeZone.getRawOffset() / (1000 * 60 * 60);

		String timeZoneDisplay = timeZone.getID();
		if (timeZoneDisplay.indexOf("/") > -1) {
			timeZoneDisplay = timeZoneDisplay.substring(
					timeZoneDisplay.indexOf("/") + 1, timeZoneDisplay.length());
		} else {
			timeZoneDisplay = "";
		}

		String strOffSetNum = (Math.abs(offset) < 10) ? ("0" + Math.abs(offset))
				: Math.abs(offset) + "";
		timeZoneDisplay = timeZoneDisplay.replace("_", " ");
		String strOffset = strOffSetNum + ":" + "00) " + timeZoneDisplay;
		return (offset < 0) ? "(GMT-" + strOffset : "(GMT+" + strOffset;
	}

	public static void main(String[] args) {
		System.out.println(TimeZone.getDefault().getDisplayName());
		String[] availableIDs = TimeZone.getAvailableIDs();
		for (int i = 0; i < availableIDs.length; i++) {
			String timezoneId = availableIDs[i];
			TimeZone timeZone = TimeZone.getTimeZone(timezoneId);

			if (!getArea(timeZone.getID()).equals("")) {
				System.out
						.println("timeMap.put(\"" + (i + 1)
								+ "\", new TimezoneExt(\"" + (i + 1) + "\", \""
								+ getOffsetString(timeZone) + "\",\""
								+ getArea(timeZone.getID()) + "\"," + "\""
								+ timeZone.getID() + "\")); "
								+ timeZone.getRawOffset());
			}
		}

	}
}
