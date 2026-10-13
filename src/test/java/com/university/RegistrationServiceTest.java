package com.university;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RegistrationServiceTest {

    @Test
    void shouldRegisterStudentForValidCourse() {

        Student student = new Student("S101", "Anita");

        Course course = new Course(
                "CS301",
                "Data Structures",
                4,
                30,
                true,
                null
        );

        RegistrationService service = new RegistrationService();

        boolean result = service.register(student, course);

        assertTrue(result);
        assertTrue(student.isRegistered("CS301"));
        assertEquals(4, student.getTotalCredits());
        assertEquals(1, course.getCurrentEnrollment());
    }

    @Test
    void shouldRejectInactiveCourse() {

        Student student = new Student("S101", "Anita");

        Course course = new Course(
                "CS301",
                "Data Structures",
                4,
                30,
                false,
                null
        );

        RegistrationService service = new RegistrationService();

        boolean result = service.register(student, course);

        assertFalse(result);
        assertFalse(student.isRegistered("CS301"));
    }

    @Test
    void shouldRejectMissingPrerequisite() {

        Student student = new Student("S101", "Anita");

        Course course = new Course(
                "CS401",
                "Advanced Algorithms",
                4,
                30,
                true,
                "CS301"
        );

        RegistrationService service = new RegistrationService();

        boolean result = service.register(student, course);

        assertFalse(result);
        assertFalse(student.isRegistered("CS401"));
    }

    @Test
    void shouldAllowRegistrationWhenPrerequisiteIsCompleted() {

        Student student = new Student("S101", "Anita");
        student.addCompletedPrerequisite("CS301");

        Course course = new Course(
                "CS401",
                "Advanced Algorithms",
                4,
                30,
                true,
                "CS301"
        );

        RegistrationService service = new RegistrationService();

        boolean result = service.register(student, course);

        assertTrue(result);
        assertTrue(student.isRegistered("CS401"));
    }

    @Test
    void shouldRejectDuplicateRegistration() {

        Student student = new Student("S101", "Anita");

        Course course = new Course(
                "CS301",
                "Data Structures",
                4,
                30,
                true,
                null
        );

        RegistrationService service = new RegistrationService();

        assertTrue(service.register(student, course));

        boolean secondAttempt = service.register(student, course);

        assertFalse(secondAttempt);
        assertEquals(4, student.getTotalCredits());
        assertEquals(1, course.getCurrentEnrollment());
    }

    @Test
    void shouldAllowRegistrationWhenTotalCreditsReach24() {
    Student student = new Student("S401", "Test Student");
    RegistrationService service = new RegistrationService();

    for (int i = 1; i <= 5; i++) {
        student.addRegistration("EXIST" + i, 4);
    }

    Course course = new Course(
        "CS401", "Computer Science", 4, 30, true, null
    );

    boolean result = service.register(student, course);

    assertTrue(result);
    assertEquals(24, student.getTotalCredits());
    assertTrue(student.isRegistered("CS401"));
    assertEquals(1, course.getCurrentEnrollment());
}

    @Test
    void shouldRejectRegistrationAbove24CreditsWithoutChangingState() {
    Student student = new Student("S402", "Test Student");
    RegistrationService service = new RegistrationService();

    for (int i = 1; i <= 6; i++) {
        student.addRegistration("EXIST" + i, 4);
    }

    Course course = new Course(
        "CS402", "Computer Science", 4, 30, true, null
    );

    int creditsBefore = student.getTotalCredits();
    int enrollmentBefore = course.getCurrentEnrollment();

    boolean result = service.register(student, course);

    assertFalse(result);
    assertFalse(student.isRegistered("CS402"));
    assertEquals(creditsBefore, student.getTotalCredits());
    assertEquals(enrollmentBefore, course.getCurrentEnrollment());
}

    @Test
    void shouldRejectRegistrationWhenCourseIsFullWithoutChangingState() {
    Course course = new Course(
        "CS101", "Computer Science", 4, 2, true, null
    );

    course.incrementEnrollment();
    course.incrementEnrollment();

    Student student = new Student("S101", "Test Student");
    RegistrationService service = new RegistrationService();

    int enrollmentBefore = course.getCurrentEnrollment();
    int creditsBefore = student.getTotalCredits();

    boolean result = service.register(student, course);

    assertFalse(result);
    assertFalse(student.isRegistered(course.getCourseCode()));
    assertEquals(creditsBefore, student.getTotalCredits());
    assertEquals(enrollmentBefore, course.getCurrentEnrollment());
}

    @Test
    void shouldRegisterWhenExactlyOneSeatRemains() {
    Course course = new Course(
        "CS102", "Computer Science", 4, 2, true, null
    );

    course.incrementEnrollment();

    Student student = new Student("S102", "Test Student");
    RegistrationService service = new RegistrationService();

    boolean result = service.register(student, course);

    assertTrue(result);
    assertTrue(student.isRegistered(course.getCourseCode()));
    assertEquals(4, student.getTotalCredits());
    assertEquals(2, course.getCurrentEnrollment());
}
}