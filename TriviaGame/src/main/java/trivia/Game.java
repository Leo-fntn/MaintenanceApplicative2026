package trivia;

import java.util.ArrayList;
import java.util.LinkedList;

// REFACTOR ME
public class Game implements IGame {
   ArrayList<Player> players = new ArrayList<>();

   LinkedList<String> popQuestions = new LinkedList<>();
   LinkedList<String> scienceQuestions = new LinkedList<>();
   LinkedList<String> sportsQuestions = new LinkedList<>();
   LinkedList<String> rockQuestions = new LinkedList<>();

   int currentPlayer = 0;
   boolean isGettingOutOfPenaltyBox;

   public Game() {
      for (int i = 0; i < 50; i++) {
         popQuestions.addLast("Pop Question " + i);
         scienceQuestions.addLast(("Science Question " + i));
         sportsQuestions.addLast(("Sports Question " + i));
         rockQuestions.addLast("Rock Question " + i);
      }
   }

   public boolean isPlayable() {
      return (howManyPlayers() >= 2);
   }

   public boolean add(String playerName) {
      players.add(new Player(playerName));

      System.out.println(playerName + " was added");
      System.out.println("They are player number " + players.size());
      return true;
   }

   public int howManyPlayers() {
      return players.size();
   }

   public void roll(int roll) {
      if (!isPlayable()) {
         System.err.println("Game is not playable yet (need at least 2 players)");
         return;
      }
      String playerName = players.get(currentPlayer).getName();
      System.out.println(playerName + " is the current player");
      System.out.println("They have rolled a " + roll);

      Player player = players.get(currentPlayer);

      if (player.isInPenaltyBox()) {
         if (roll % 2 != 0) {
            isGettingOutOfPenaltyBox = true;
            //player.setInPenaltyBox(false);

            System.out.println(playerName + " is getting out of the penalty box");
            player.move(roll);

            System.out.println(playerName
                    + "'s new location is "
                    + player.getPlace());
            System.out.println("The category is " + currentCategory());
            askQuestion();
         } else {
            System.out.println(playerName + " is not getting out of the penalty box");
            isGettingOutOfPenaltyBox = false;
         }

      } else {
         player.move(roll);

         System.out.println(playerName
                 + "'s new location is "
                 + player.getPlace());
         System.out.println("The category is " + currentCategory());
         askQuestion();
      }

   }

   private void askQuestion() {
      switch (currentCategory()) {
         case "Pop":
            System.out.println(popQuestions.removeFirst());
            break;
         case "Science":
            System.out.println(scienceQuestions.removeFirst());
            break;
         case "Sports":
            System.out.println(sportsQuestions.removeFirst());
            break;
         case "Rock":
            System.out.println(rockQuestions.removeFirst());
            break;
      }
   }


   private String currentCategory() {
      int place = players.get(currentPlayer).getPlace();
      if (place - 1 == 0) return "Pop";
      if (place - 1 == 4) return "Pop";
      if (place - 1 == 8) return "Pop";
      if (place - 1 == 1) return "Science";
      if (place - 1 == 5) return "Science";
      if (place - 1 == 9) return "Science";
      if (place - 1 == 2) return "Sports";
      if (place - 1 == 6) return "Sports";
      if (place - 1 == 10) return "Sports";
      return "Rock";
   }

   public boolean handleCorrectAnswer() {
      if (!isPlayable()) {
         System.err.println("Game is not playable yet (need at least 2 players)");
         return true;
      }
      Player player = players.get(currentPlayer);
      String playerName = player.getName();
      if (player.isInPenaltyBox() && !isGettingOutOfPenaltyBox) {
         currentPlayer++;
         if (currentPlayer == players.size()) currentPlayer = 0;
         return true;
      } else {
         System.out.println("Answer was correct!!!!");
         player.addCoin();
         System.out.println(playerName
                 + " now has "
                 + player.getPurse()
                 + " Gold Coins.");

         boolean winner = didPlayerWin();
         currentPlayer++;
         if (currentPlayer == players.size()) currentPlayer = 0;

         return winner;
      }
   }

   public boolean wrongAnswer() {
      if (!isPlayable()) {
         System.err.println("Game is not playable yet (need at least 2 players)");
         return true;
      }
      String playerName = players.get(currentPlayer).getName();
      System.out.println("Question was incorrectly answered");
      System.out.println(playerName + " was sent to the penalty box");
      players.get(currentPlayer).enterPenaltyBox();

      currentPlayer++;
      if (currentPlayer == players.size()) currentPlayer = 0;
      return true;
   }


   private boolean didPlayerWin() {
      return !(players.get(currentPlayer).getPurse() == 6);
   }
}
