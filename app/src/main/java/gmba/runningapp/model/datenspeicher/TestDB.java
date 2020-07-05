package gmba.runningapp.model.datenspeicher;

import gmba.runningapp.model.datenspeicher.classes.*;

import java.util.LinkedList;
import java.util.List;

/**
 * this is used for test purposes only
 */

public class TestDB implements Data{
        private List<User> uList;
        private List<String> userNames;
        private String currentUser = "default";

        public TestDB(){
            List<User> users = new LinkedList<>();
            LinkedList<Run> runs = new LinkedList<>();

            LinkedList<Coordinates> coordsBerlin = new LinkedList<>();
            coordsBerlin.add(new CoordinatesImpl(52.5294, 13.1923));
            coordsBerlin.add(new CoordinatesImpl(52.5293, 13.1916));
            coordsBerlin.add(new CoordinatesImpl(52.5291, 13.1904));
            coordsBerlin.add(new CoordinatesImpl(52.5289, 13.1891));
            coordsBerlin.add(new CoordinatesImpl(52.5288, 13.1873));
            coordsBerlin.add(new CoordinatesImpl(52.5309, 13.1795));
            coordsBerlin.add(new CoordinatesImpl(52.5311, 13.1746));


            Run run1 = new RunImpl(1,10,10,10,"24-12-1980",coordsBerlin);
            Run run2 = new RunImpl(2,15,1,1,"20-12-1982",coordsBerlin);
            Run run3 = new RunImpl(3,20,10,20,"18-08-1990",coordsBerlin);
            runs.add(run1);
            runs.add(run2);
            runs.add(run3);

            User u1 = new UserImpl(1,"Frank",0,runs);

            users.add(u1);

            this.uList = users;

        }


        @Override
        public void saveUserData(User user) {
            uList.add(user);
            userNames.add(user.getName());
        }

        @Override
        public User loadUserData(String userName) {
            for(User u: uList){
                if(u.getName().equals(userName)){
                    return u;
                }
            }
            return null;
        }

        @Override
        public void deleteUserData(String userName) {
            for(User u: uList){
                if(u.getName().equals(userName)){
                    uList.remove(u);
                    userNames.remove(u.getName());
                }
            }
        }

        @Override
        public void saveUserList(List<String> userList) {
            this.userNames = userList;
        }

        @Override
        public List<String> loadUserList() {
            return this.userNames;
        }

        @Override
        public void saveUserRunList(String userName, List<Integer> idList) {
            for(User u : uList){
                if(u.getName().equals(userName)){
                }
            }
        }

        @Override
        public List<Integer> loadUserRunList(String userName) {
            return null;
        }

        @Override
        public void saveUserRun(Run run, String userName) {
            for(User u : uList){
                if(u.getName().equals(userName)){
                    u.getHistory().add(run);
                }
            }
        }

        @Override
        public Run loadUserRun(String userName, int runId) {
            for(User u : uList){
                if(u.getName().equals(userName)){
                    for(Run r: u.getHistory()){
                        if(r.getId() == runId){
                            return r;
                        }
                    }
                }
            }
            return null;
        }

        @Override
        public void deleteUserRun(String userName, int runId) {
            for(User u : uList){
                if(u.getName().equals(userName)){
                    boolean found = false;
                    for(Run r: u.getHistory()){
                        if(r.getId() == runId){
                            found = true;
                            u.getHistory().remove(r);
                        }
                    }
                }
            }
        }

        @Override
        public void setCurrentUser(String name) {
            this.currentUser= name;
        }

        @Override
        public String getCurrentUser() {
            return this.currentUser;
        }
    }

