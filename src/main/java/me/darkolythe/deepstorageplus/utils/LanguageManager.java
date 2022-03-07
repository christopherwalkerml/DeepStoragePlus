package me.darkolythe.deepstorageplus.utils;

import me.darkolythe.deepstorageplus.DeepStoragePlus;
import org.bukkit.ChatColor;

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
        translateMap.put("sorterwalls", main.getConfig().getString("sorterwalls"));
        translateMap.put("emptystorageblock", main.getConfig().getString("emptystorageblock"));
        translateMap.put("empty", main.getConfig().getString("empty"));
        translateMap.put("storagecontainer", main.getConfig().getString("storagecontainer"));
        translateMap.put("creativestoragecontainer", main.getConfig().getString("creativestoragecontainer"));
        translateMap.put("currentstorage", main.getConfig().getString("currentstorage"));
        translateMap.put("currenttypes", main.getConfig().getString("currenttypes"));
        translateMap.put("onlydefaultitems", main.getConfig().getString("onlydefaultitems"));
        translateMap.put("specialcrafting", main.getConfig().getString("specialcrafting"));
        translateMap.put("storagecell", main.getConfig().getString("storagecell"));
        translateMap.put("storageloader", main.getConfig().getString("storageloader"));
        translateMap.put("linkmodule", main.getConfig().getString("linkmodule"));
        translateMap.put("sorterloader", main.getConfig().getString("sorterloader"));
        translateMap.put("clickempty", main.getConfig().getString("clickempty"));
        translateMap.put("tocreatedsu", main.getConfig().getString("tocreatedsu"));
        translateMap.put("tocreatesorter", main.getConfig().getString("tocreatesorter"));
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
        translateMap.put("sortercreate", main.getConfig().getString("sortercreate"));
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
        translateMap.put("shiftswap", main.getConfig().getString("shiftswap"));
        translateMap.put("world", main.getConfig().getString("world"));
        translateMap.put("locked", main.getConfig().getString("locked"));
        translateMap.put("unlocked", main.getConfig().getString("unlocked"));
        translateMap.put("leftclicktoadd", main.getConfig().getString("leftclicktoadd"));
        translateMap.put("rightclicktoremove", main.getConfig().getString("rightclicktoremove"));
        translateMap.put("notallowedtoopen", main.getConfig().getString("notallowedtoopen"));
        translateMap.put("entername", main.getConfig().getString("entername"));
        translateMap.put("iospeed", main.getConfig().getString("iospeed"));
        translateMap.put("ioupgrade", main.getConfig().getString("ioupgrade"));
        translateMap.put("clicktoupgrade", main.getConfig().getString("clicktoupgrade"));
        translateMap.put("upgradesuccess", main.getConfig().getString("upgradesuccess"));
        translateMap.put("upgradefail", main.getConfig().getString("upgradefail"));
        translateMap.put("owner", main.getConfig().getString("owner"));
        translateMap.put("notowner", main.getConfig().getString("notowner"));
        translateMap.put("typecancel", main.getConfig().getString("typecancel"));
        translateMap.put("maxdistance", main.getConfig().getString("maxdistance"));
        translateMap.put("toofar", main.getConfig().getString("toofar"));
        translateMap.put("givecommand", main.getConfig().getString("givecommand"));
        translateMap.put("emptysorterblock", main.getConfig().getString("emptysorterblock"));
    }

    public static String getValue(String key) {
        return ChatColor.translateAlternateColorCodes('&', translateMap.getOrDefault(key, "[Invalid Translate Key]"));
    }
}
