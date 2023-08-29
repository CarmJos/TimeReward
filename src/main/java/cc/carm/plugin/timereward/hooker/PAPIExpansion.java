package cc.carm.plugin.timereward.hooker;

import cc.carm.lib.easyplugin.papi.EasyPlaceholder;
import cc.carm.lib.easyplugin.papi.handler.PlaceholderHandler;
import cc.carm.plugin.timereward.TimeRewardAPI;
import cc.carm.plugin.timereward.data.RewardContents;
import cc.carm.plugin.timereward.data.UserData;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.function.BiFunction;
import java.util.function.Function;

public class PAPIExpansion extends EasyPlaceholder {

    public PAPIExpansion(@NotNull JavaPlugin plugin, @NotNull String rootIdentifier) {
        super(plugin, rootIdentifier);

        handle("time", userHandler(UserData::getAllSeconds));

        handle("reward",
                rewardHandler(RewardContents::getDisplayName),
                Collections.singletonList("<奖励ID>")
        );

        handle("claimed", userHandler((user, args) -> {
            if (args.length < 1) return "请填写奖励ID";
            else return user.isClaimed(args[0]);
        }), Collections.singletonList("<奖励ID>"));

        handle("claimable", (offlinePlayer, args) -> {
            if (offlinePlayer == null || !offlinePlayer.isOnline()) return "加载中...";
            if (args.length < 1) return "请填写奖励ID";

            RewardContents reward = TimeRewardAPI.getRewardManager().getReward(args[0]);
            if (reward == null) return "奖励不存在";

            return TimeRewardAPI.getRewardManager().isClaimable((Player) offlinePlayer, reward);
        }, Collections.singletonList("<奖励ID>"));

        handle("version", (player, args) -> getVersion());
    }

    protected <R> PlaceholderHandler userHandler(Function<UserData, R> userFunction) {
        return userHandler((user, args) -> userFunction.apply(user));
    }

    protected <R> PlaceholderHandler userHandler(BiFunction<UserData, String[], R> userFunction) {
        return (player, args) -> {
            if (player == null || !player.isOnline()) return "加载中...";
            return userFunction.apply(TimeRewardAPI.getUserManager().getData((Player) player), args);
        };
    }

    protected <R> PlaceholderHandler rewardHandler(Function<RewardContents, R> function) {
        return (player, args) -> {
            if (args.length < 1) return "请填写奖励ID";
            String rewardName = args[0];
            RewardContents contents = TimeRewardAPI.getRewardManager().getReward(rewardName);
            if (contents == null) return "奖励不存在";
            return function.apply(contents);
        };
    }

}
