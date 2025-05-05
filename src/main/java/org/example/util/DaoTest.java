package org.example.util;

import org.example.dao.WorkoutSessionDao;
import org.example.dao.ExerciseEntryDao;
import org.example.model.WorkoutSession;
import org.example.model.ExerciseEntry;
import org.example.model.ExerciseType;

import java.time.LocalDateTime;
import java.util.List;

public class DaoTest {
    public static void main(String[] args) throws Exception {
        WorkoutSessionDao wsDao = new WorkoutSessionDao();
        ExerciseEntryDao eeDao = new ExerciseEntryDao();

        WorkoutSession s = wsDao.insert(
                new WorkoutSession(1, LocalDateTime.now(), "Test session"));
        System.out.println("Session ID=" + s.getId());

        List<ExerciseEntry> list = eeDao.findBySession(s.getId());
        list.forEach(e -> System.out.println(e.getType() +
                " sets=" + e.getSets() +
                " reps=" + e.getReps() +
                " weight=" + e.getWeight() +
                " dur=" + e.getDurationSeconds()));
    }
}
