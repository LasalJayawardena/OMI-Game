package Game;

import java.util.ArrayList;
import java.util.Collections;

public class LeaderBoard {

    //  Stores the list of players for which th leaderboard is made for.
    private final ArrayList<Player> playerList;

    public LeaderBoard(ArrayList<Player> playerList)
    {
        this.playerList = playerList;
    }

    /*
    *  Gets winner of Current Leaderboard
    */
    public Player getWinner()
    {
        //  Sorts all players and then get the first player from that.
        //  Make use of Comparable Interface Implemented in Player Class.
        this.playerList.sort(Collections.reverseOrder());
        return this.playerList.get(0);
    }

    /*
    *  Increment score for a player by given amount
    */
    public void updateScore(Player player, int increment)
    {
        player.addScore(increment);
    }

    /*
    *  Helps to get the Relative Rank which is an advanced concept than the normal rank
    *  In this if players have the same score they are considered "tied"
    *  So if the two highest players have same the score both of them are considered to have rank 1.
    *  And the third player will have a rank of three.
    */
    public int getRank(Player player)
    {
        // Sort the playerList by score
        this.playerList.sort(Collections.reverseOrder());
        int prevScore = this.playerList.get(0).getScore();
        // rank keeps track of conventional Rank
        // rel_rank is for the relative rank.
        int rank = 0;
        int relRank = 1;
        for(Player P : this.playerList)
        {
            rank ++;
            relRank ++;
            if(P.getScore() == prevScore)
            {
                relRank --;
            }else{
                relRank = rank;
            }
            if(P == player)
            {
                return relRank;
            }
            prevScore = P.getScore();
        }
        return 1;
    }

    /*
    *  Gives current playerList
    */
    public ArrayList<Player> getPlayerList() {
        return this.playerList;
    }

    @Override
    public String toString() {
        //  Print all Player names and corresponding Scores.
        StringBuilder result= new StringBuilder();
        this.playerList.sort(Collections.reverseOrder());
        for(Player P : this.playerList )
        {
            result.append(P.getName()).append(" ").append(P.getScore()).append(" ").append("\t");
        }
        return result.toString();
    }

    public static void main(String[] args) {
        // Preliminary tests.
        UserPlayer gameUser = new UserPlayer("Player");
        RobotPlayer R1 = new RobotPlayer("Robot1");
        RobotPlayer R2 = new RobotPlayer("Robot2");
        RobotPlayer R3 = new RobotPlayer("Robot3");
        ArrayList<Player> PL= new ArrayList<>();
        PL.add(gameUser);
        PL.add(R1);
        PL.add(R2);
        PL.add(R3);
        LeaderBoard LB = new LeaderBoard(PL);
        LB.updateScore(gameUser,2);
        LB.updateScore(R2,2);
        LB.updateScore(R1,3);

        System.out.println(LB.getRank(gameUser));
        System.out.println(LB.getRank(R1));
        System.out.println(LB.getRank(R2));
        System.out.println(LB.getRank(R3));

    }

}
