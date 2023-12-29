package com.my.business;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalTime;
import java.time.LocalDate;
import java.time.Duration;

import com.my.module.Gymnasium;
import com.my.module.Person;

public class GameArrangement {
    // 运动员列表
    private List<Person> playerList;
    // 裁判列表
    private List<Person> refereeList;
    // 开始日期、结束日期
    private LocalDate startDate;
    private LocalDate endDate;
    // 每天比赛总时长，以分钟计算
    private int dailyMatchMins;
    // 每场比赛时间
    private int matchDurationOnce;
    // 比赛场馆
    private List<Gymnasium> gList;
    // 每天比赛时间段列表，一天可以分段时间进行比赛
    private List<Duration> dailyMatchDurationList;

    public static void main(String[] args) {
        List<String> players = new ArrayList<>();
        players.add("Player 1");
        players.add("Player 2");
        players.add("Player 3");

        // Add more players as needed
        new GameArrangement().doubleRoundRobinTournament(players);
    }
    /*
    * 单循环比赛分组
      如n为奇数，n+1补齐偶数
     *偶数后，比赛轮次=n-1;
      场数=n(n-1)/2
    */
    public void singleRoundRobinTournament(List players){
        if (players.size() % 2 != 0) {
            players.add("Bye"); // If the number of players is odd, add a bye
        }

        int n = players.size();
        int rounds = n - 1;
        int matches = n * (n - 1) / 2;
        System.out.println("比赛轮次：" + rounds);
        System.out.println("比赛场数：" + matches);

        for (int i = 0; i < players.size() - 1; i++) {
            System.out.println("Round " + (i + 1) + ":");
            for (int j = 0; j < players.size() / 2; j++) {
                System.out.println(players.get(j) + " vs " + players.get(players.size() - 1 - j));
            }
            players.add(1, players.remove(players.size() - 1)); // Rotate the array
        }
    }

    /*
     * 双循环比赛分组
     * 单循环循环2次
     */
    public void doubleRoundRobinTournament(List players){
        int n = players.size();
        int rounds = n;
        int matches = n * (n - 1);
        System.out.println("比赛轮次：" + rounds);
        System.out.println("比赛场数：" + matches);

        for (int i = 0; i < players.size(); i++) {
            String player1 = (String) players.get(i);
            for (int j = 0; j < players.size(); j++) {
                String player2 = (String) players.get(j);
                if (!player1.equals(player2)) {
                    System.out.println(player1 + " vs " + player2);
                }
            }
        }
    }

    // 计算时间差得出分钟数
    public long calTimeDiffInMinutes(LocalTime startTime, LocalTime endTime) {
        Duration duration = Duration.between(startTime, endTime);
        return duration.toMinutes();
    }

    // 计算相差天数
    public long calTimeDiffInDays(LocalDate start, LocalDate end){
        return end.toEpochDay() - start.toEpochDay();
    }

}
