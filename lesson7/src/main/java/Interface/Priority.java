package Interface;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Priority {
    enum TestPriority {
        priority_0 (0),
        priority_1 (1),
        priority_2 (2),
        priority_3 (3),
        priority_4 (4),
        priority_5 (5),
        priority_6 (6),
        priority_7 (7),
        priority_8 (8),
        priority_9 (9);


        private int title;

        TestPriority(int title) {
            this.title = title;
        }
        public int getTitle() {
            return title;
        }

        @Override
        public String toString() {
            return "DayOfWeek{" +
                    "title='" + title + '\'' +
                    '}';
        }
    }
}
