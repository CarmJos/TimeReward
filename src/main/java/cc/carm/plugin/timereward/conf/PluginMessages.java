package cc.carm.plugin.timereward.conf;

import cc.carm.lib.configuration.core.ConfigurationRoot;
import cc.carm.lib.configuration.core.annotation.HeaderComment;
import cc.carm.lib.easyplugin.utils.ColorParser;
import cc.carm.lib.mineconfiguration.bukkit.builder.message.CraftMessageListBuilder;
import cc.carm.lib.mineconfiguration.bukkit.builder.message.CraftMessageValueBuilder;
import cc.carm.lib.mineconfiguration.bukkit.builder.title.TitleConfigBuilder;
import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredMessage;
import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredMessageList;
import cc.carm.lib.mineconfiguration.bukkit.value.ConfiguredTitle;
import de.themoep.minedown.MineDown;
import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;


@HeaderComment({
        "TimeReward 在线奖励插件的消息配置文件",
        "如特定的消息不需要任何提示，可直接留下单行空内容消息。",
        "支持 支持 &+颜色代码(原版颜色)、§(#XXXXXX)(RGB颜色) 与 &<#XXXXXX>(前后标注RGB颜色渐变)。",
        " "
})
public class PluginMessages extends ConfigurationRoot {

    public static @NotNull CraftMessageListBuilder<BaseComponent[]> list() {
        return ConfiguredMessageList.create(getParser())
                .whenSend((sender, message) -> message.forEach(m -> sender.spigot().sendMessage(m)));
    }

    public static @NotNull CraftMessageValueBuilder<BaseComponent[]> value() {
        return ConfiguredMessage.create(getParser())
                .whenSend((sender, message) -> sender.spigot().sendMessage(message));
    }

    public static @NotNull TitleConfigBuilder title() {
        return ConfiguredTitle.create().whenSend((player, in, stay, out, line1, line2) -> player.sendTitle(line1, line2, in, stay, out));
    }

    public static @NotNull BiFunction<CommandSender, String, BaseComponent[]> getParser() {
        return (sender, message) -> {
            if (sender instanceof Player) message = PlaceholderAPI.setPlaceholders((Player) sender, message);
            return MineDown.parse(ColorParser.parse(message));
        };
    }

    public static final ConfiguredMessageList<BaseComponent[]> COMMAND_USAGE = list().defaults(
            "&6&l在线奖励 &f指令帮助",
            "&8#&f reload",
            "&8-&7 重载插件配置文件。",
            "&8#&f user &6<玩家>",
            "&8-&7 查看用户的在线时长信息与奖励领取情况。",
            "&8#&f listUserData",
            "&8-&7 列出所有奖励与条件。",
            "&8#&f test &6<奖励ID>",
            "&8-&7 测试执行奖励配置的指令。"
    ).build();


    public static class LIST extends ConfigurationRoot {

        public static final ConfiguredMessageList<BaseComponent[]> HEADER = list().defaults(
                "&6&l在线奖励 &f奖励列表 &7(共%(amount)个)"
        ).params("amount").build();

        public static final ConfiguredMessageList<BaseComponent[]> OBJECT = list().defaults(
                "&8# &f%(id)",
                "&8- &7奖励名称 &f%(name)",
                "&8- &7领取时间 &f&e%(time)&f秒"
        ).params("id", "name", "time").build();

        public static final ConfiguredMessageList<BaseComponent[]> OBJECT_PERM = list().defaults(
                "&8# &f%(id)",
                "&8- &7奖励名称 &f%(name)",
                "&8- &7领取时间 &f&e%(time)&f秒",
                "&8- &7需要权限 &f%(permission)"
        ).params("id", "name", "time", "permission").build();

    }

    public static final ConfiguredMessageList<BaseComponent[]> USER_INFO = list().defaults(
            "&f玩家 &6%(player) &f已在线&e%(time)&f秒，共领取了 &e%(amount)&f 次奖励。",
            "&7已领取的奖励列表如下：&r%(rewards) &7。"
    ).params("player", "time", "amount", "rewards").build();

    public static final ConfiguredMessageList<BaseComponent[]> COMMAND_LIST = list().defaults(
            "&f正在执行奖励 %(award) 的指令列表..."
    ).params("award").build();

    public static final ConfiguredMessageList<BaseComponent[]> NOT_ONLINE = list().defaults(
            "&f玩家 &e%(player) &f并不在线。"
    ).params("player").build();

    public static final ConfiguredMessageList<BaseComponent[]> NOT_EXISTS = list().defaults(
            "&f奖励 &e%(award) &f并不存在。"
    ).params("award").build();

}
