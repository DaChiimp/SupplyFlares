package com.dachiimp.supplyflares;

import com.dachiimp.supplyflares.Commands.*;
import com.dachiimp.supplyflares.Config.SetupMethods;
import com.dachiimp.supplyflares.Config.setupCommands;
import com.dachiimp.supplyflares.Listeners.*;
import com.dachiimp.supplyflares.Util.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by DaChiimp on 6/19/2016. For SupplyFlares
 */
public class SupplyFlares extends JavaPlugin implements Listener {
    public static boolean debug = false;
    public Plugin _plugin = null;
    public Logger logger;
    public SetupMethods setupMethods;
    public setupCommands sc;
    BlockPlaceEventListener blockPlaceClass;
    public Methods methods;
    EntityDamageByEntityEventListener ed;
    InteractEventListener iel;
    onJoinEventListener joinL;
    fuckClearLagg fclg;
    public onCommandExecutorClass cmdE;
    public GiveCommand giveCommand;
    public ConsoleCommands consoleStuff;
    public ReloadCommand reloadCommand;
    public ClearCommand clearCommand;
    public ListCommand listCommand;
    public HelpCommand helpCommand;
    public FixCommand fixCommand;
    public InfoCommand infoCommand;
    public SpawnRandomCommand spawnRandomCommand;


    public boolean disabled = false;


    public String prefix;
    public int height;
    public int coolDown;
    public int listCooldown;
    public int fireworkInterval;
    public int minRadius;
    public int maxRadius;
    public int vicinityMinRadius;
    public int vicinityMaxRadius;
    public int randomCrateSpawnInterval;
    public int randomCrateSpawnMin;
    public int randomCrateSpawnMax;
    public int randomCrateSpawnMinRequiredPlayers;
    public boolean instant = false;
    public boolean giveMap = false;
    public boolean randomCrateSpawn = false;
    public boolean claimOnlyInWarzone = false;
    public ArrayList<UUID> onCoolDown = new ArrayList<>();
    public ArrayList<UUID> onListCoolDown = new ArrayList<>();
    public HashMap<Location, SupplyCrate> drops = new HashMap<>();
    public HashMap<Location, Entity> entityMap = new HashMap<>();

    public ArrayList<Entity> fallingCrates = new ArrayList<>();
    public ArrayList<SupplyCrate> crates = new ArrayList<>();
    public HashMap<String,RandomSupplyCrate> randomCrateSpawnArray = new HashMap<>();
    /*
        TODO: Get rid of this in-efficient shit and change it into class storing
     */
    public boolean newestVersionBol = true;
    /*
        ////////////////////
        /    Crate stuff   /
        ////////////////////
     */

    public HashMap<String, String> messages = new HashMap<>();
    private String currentVersion = this.getDescription().getVersion();

    // TODO: Add ids to manually spawn by id for random crate spawn array

    public void onEnable() {
        _plugin = this;
        getCommand("flares").setExecutor(cmdE = new onCommandExecutorClass(this));
        reload();
        logger.log("Version " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + currentVersion + ChatColor.AQUA + " ENABLED");
        setupDataFolder();
        PluginManager pm = Bukkit.getServer().getPluginManager();
        pm.registerEvents(this, this);
        pm.registerEvents(ed, this);
        pm.registerEvents(iel, this);
        pm.registerEvents(blockPlaceClass, this);
        pm.registerEvents(new BlockBreakEventListener(this), this);
        pm.registerEvents(joinL, this);
        if (Bukkit.getPluginManager().isPluginEnabled("ClearLag")) {
            pm.registerEvents(fclg, this);
            logger.log("Registered events to stop clear lagg from fucking with me");
        }
        setupConfig();

        setupMethods.setupCrates();
        setupMethods.setupStrings();

        sc.doIt();

        if (!disabled) {
            logger.log(ChatColor.LIGHT_PURPLE + "Found no issues with the files, all features will function");
        } else {
            logger.log(ChatColor.RED + "" + ChatColor.BOLD + " Issues were found, check the console for more information");
        }


        Bukkit.getScheduler().scheduleSyncDelayedTask(_plugin, new Runnable() {
            @Override
            public void run() {
                if (randomCrateSpawn) {
                    methods.randomCrateSpawnEvents();
                }
            }
        }, 20L);

        Bukkit.getScheduler().scheduleSyncDelayedTask(_plugin, new Runnable() {
            @Override
            public void run() {
                checkForUpdate();
            }
        }, 5L);


    }

    public void onDisable() {
        if (drops.size() > 0) {
            for (Location loc : drops.keySet()) {
                entityMap.get(loc).remove();

                loc.getBlock().setType(Material.AIR);

                logger.log("Removed Supply Drop @ " + loc.getX() + " | " + loc.getY() + " | " + loc.getZ());
            }

            drops.clear();
            entityMap.clear();

        }
        logger.log("Version " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + currentVersion + ChatColor.AQUA + " DISABLED");
        unLoad();
        _plugin = null;
    }

    private void unLoad() {
        logger = null;
        setupMethods = null;
        sc = null;
        blockPlaceClass = null;
        methods = null;
        ed = null;
        iel = null;
        joinL = null;
        fclg = null;
        cmdE = null;
        giveCommand = null;
        consoleStuff = null;
        reloadCommand = null;
        clearCommand = null;
        listCommand = null;
        helpCommand = null;
        infoCommand = null;
        fixCommand = null;
        spawnRandomCommand = null;
    }

    private void reload() {
        logger = new Logger(this);
        giveCommand = new GiveCommand(this,cmdE);
        consoleStuff = new ConsoleCommands(this,cmdE);
        reloadCommand = new ReloadCommand(this,cmdE);
        clearCommand = new ClearCommand(this,cmdE);
        listCommand = new ListCommand(this,cmdE);
        helpCommand = new HelpCommand(this,cmdE);
        infoCommand = new InfoCommand(this,cmdE);
        fixCommand = new FixCommand(this,cmdE);
        spawnRandomCommand = new SpawnRandomCommand(this,cmdE);
        setupMethods = new SetupMethods(this);
        sc = new setupCommands();
        methods = new Methods(this);
        blockPlaceClass = new BlockPlaceEventListener(this);
        ed = new EntityDamageByEntityEventListener(this);
        iel = new InteractEventListener(this);
        joinL = new onJoinEventListener(this);
        fclg = new fuckClearLagg(this);
    }

    public void setupConfig() {
        File file = new File(_plugin.getDataFolder(), "config.yml");
        File file2 = new File(_plugin.getDataFolder(), "messages.yml");

        if (!file.exists()) {
            logger.log("Created config as one didn't exist");
            _plugin.saveDefaultConfig();
        }

        if (!file2.exists()) {
            logger.log("Created messages.yml as one didn't exist");
            _plugin.saveResource("messages.yml", false);
        }
    }

    private void setupDataFolder() {
        File dir = new File(getDataFolder(), "");
        if (!dir.exists()) {
            boolean d = dir.mkdirs();
            if (d) {
                logger.log("Created data folder as one didn't exist");
            } else {
                logger.log("Error creating data folder");
            }
        }
    }

    private void checkForUpdate() {
        try {
            logger.log(ChatColor.AQUA + "Checking for a new version...");
            URL url = new URL("http://www.dachiimp.com/SupplyFlares/currentVersion.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            String str;
            while ((str = br.readLine()) != null) {
                if (!str.equalsIgnoreCase(currentVersion)) {
                    newestVersionBol = false;
                    logger.log("You are not using the newest plugin version. Newest Version = " + str);
                } else {
                    logger.log("You are using the newest plugin version.");
                }
            }
            br.close();
        } catch (IOException e) {
            logger.log(ChatColor.RED + "The UpdateChecker URL is invalid! Please let me know!");
        }
    }

}
