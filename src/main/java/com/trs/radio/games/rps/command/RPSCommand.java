package com.trs.radio.games.rps.command;

import com.trs.radio.core.commands.SubCommand;
import com.trs.radio.core.provider.EmbedProvider;
import com.trs.radio.games.rps.entity.RPSGame;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.components.Button;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class RPSCommand implements SubCommand {

    Pattern pattern = Pattern.compile("<@([0-9]+)>");

    @Override
    public void execute(MessageReceivedEvent event, String... args) {
        if (args.length < 2) {
            return;
        }
        String name = args[1];
        List<Member> members = findMembersFor(name, event.getGuild());
        if (members.size() == 0) {
            EmbedBuilder builder = EmbedProvider.getError().addField("Error", "Couldn't find any players named " + args[1], false);
            event.getChannel().sendMessageEmbeds(builder.build()).queue();
            return;
        }
        if (members.size() > 1) {
            EmbedBuilder builder = EmbedProvider.getError().addField("Error", "Multiple players with the name `%s`:".formatted(args[1]), false);
            StringBuilder sb = new StringBuilder();
            for (Member member : members) {
                sb.append("- %s\n".formatted(member.getUser().getAsTag()));
            }
            builder.addField("", sb.toString(), false);
            event.getChannel().sendMessageEmbeds(builder.build()).queue();
            return;
        }
        Member member = members.get(0);
        if (member == event.getMember()) {
            EmbedBuilder builder = EmbedProvider.getError().addField("Error", "Cannot start a match against yourself!", false);
            event.getChannel().sendMessageEmbeds(builder.build()).queue();
            return;
        }

        EmbedBuilder builder = new RPSGame(event.getMember(), member).generateEmbed();

        final MessageEmbed build = builder.build();
        event.getChannel().sendMessageEmbeds(build)
                .setActionRow(
                        Button.secondary("rps_rock", "Rock✊"),
                        Button.success("rps_paper", "Paper✋"),
                        Button.primary("rps_scissor", "Scissor✌")
                ).queue();
    }

    List<Member> findMembersFor(String name, Guild guild) {
        Matcher matcher = pattern.matcher(name);
        if (matcher.find()) {
            String id = matcher.group(1);
            final Member memberById = guild.retrieveMemberById(id).complete();
            if (memberById != null) {
                return Collections.singletonList(memberById);
            }
            return new ArrayList<>();
        }
        return guild.getMembers().stream().filter(member -> {
            if (member.getUser().getAsTag().toLowerCase().contains(name.toLowerCase())) return true;
            return member.getEffectiveName().toLowerCase().contains(name.toLowerCase());
        }).collect(Collectors.toList());
    }
}
