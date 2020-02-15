package me.darkolythe.deepstorageplus.utils;

import me.darkolythe.deepstorageplus.DeepStoragePlus;

import java.util.HashMap;
import java.util.Map;

public class LanguageManager {

    private static Map<String, String> translateMap = new HashMap<>();

    public static void setup(DeepStoragePlus main) {
        translateMap.put("faileddownload", main.getConfig().getString("faileddownload"));
        translateMap.put("downloadhere", main.getConfig().getString("downloadhere"));
        translateMap.put("containersfull", main.getConfig().getString("containersfull"));
        translateMap.put("dsuioconfig", main.getConfig().getString("dsuioconfig"));
        translateMap.put("input", main.getConfig().getString("input"));
        translateMap.put("all", main.getConfig().getString("all"));
        translateMap.put("output", main.getConfig().getString("output"));
        translateMap.put("none", main.getConfig().getString("none"));
        translateMap.put("sortingby", main.getConfig().getString("sortingby"));
        translateMap.put("container", main.getConfig().getString("container"));
        translateMap.put("dsuwalls", main.getConfig().getString("dsuwalls"));
        translateMap.put("emptystorageblock", main.getConfig().getString("emptystorageblock"));
        translateMap.put("empty", main.getConfig().getString("empty"));
        translateMap.put("storagecontainer", main.getConfig().getString("storagecontainer"));
        translateMap.put("currentstorage", main.getConfig().getString("currentstorage"));
        translateMap.put("currenttypes", main.getConfig().getString("currenttypes"));
        translateMap.put("onlydefaultitems", main.getConfig().getString("onlydefaultitems"));
        translateMap.put("specialcrafting", main.getConfig().getString("specialcrafting"));
        translateMap.put("storagecell", main.getConfig().getString("storagecell"));
        translateMap.put("storageloader", main.getConfig().getString("storageloader"));
        translateMap.put("clickempty", main.getConfig().getString("clickempty"));
        translateMap.put("tocreatedsu", main.getConfig().getString("tocreatedsu"));
        translateMap.put("onetimeuse", main.getConfig().getString("onetimeuse"));
        translateMap.put("amount", main.getConfig().getString("amount"));
        translateMap.put("alpha", main.getConfig().getString("alpha"));
        translateMap.put("nomorespace", main.getConfig().getString("nomorespace"));
        translateMap.put("clicktoclear", main.getConfig().getString("clicktoclear"));
        translateMap.put("changesorting", main.getConfig().getString("changesorting"));
        translateMap.put("sortscontainer", main.getConfig().getString("sortscontainer"));
        translateMap.put("sortsalpha", main.getConfig().getString("sortsalpha"));
        translateMap.put("sortsamount", main.getConfig().getString("sortsamount"));
        translateMap.put("sortsid", main.getConfig().getString("sortsid"));
        translateMap.put("clicktostart", main.getConfig().getString("clicktostart"));
        translateMap.put("clickinput", main.getConfig().getString("clickinput"));
        translateMap.put("leaveasall", main.getConfig().getString("leaveasall"));
        translateMap.put("clickoutput", main.getConfig().getString("clickoutput"));
        translateMap.put("dsucreate", main.getConfig().getString("dsucreate"));
        translateMap.put("chestmustbedouble", main.getConfig().getString("chestmustbedouble"));
        translateMap.put("chestmustbeempty", main.getConfig().getString("chestmustbeempty"));
        translateMap.put("nopermission", main.getConfig().getString("nopermission"));
        translateMap.put("unlinked", main.getConfig().getString("unlinked"));
        translateMap.put("linked", main.getConfig().getString("linked"));
        translateMap.put("terminal", main.getConfig().getString("terminal"));
        translateMap.put("receiver", main.getConfig().getString("receiver"));
        translateMap.put("clicktolink", main.getConfig().getString("clicktolink"));
        translateMap.put("cantopenin", main.getConfig().getString("cantopenin"));
        translateMap.put("dsunolongerthere", main.getConfig().getString("dsunolongerthere"));
    }

    public static String getValue(String key) {
        if (translateMap.containsKey(key)) {
            return translateMap.get(key);
        } else {
            return "[Invalid Translate Key]";
        }
    }
}
