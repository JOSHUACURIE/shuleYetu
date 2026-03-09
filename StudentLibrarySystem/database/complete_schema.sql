-- =====================================================
-- UPDATED DATABASE SCHEMA FOR STUDENT & LIBRARY SYSTEM
-- =====================================================

DROP DATABASE IF EXISTS student_library_system;
CREATE DATABASE student_library_system;
USE student_library_system;

-- =====================================================
-- SECTION 1: CORE TABLES (Person Hierarchy)
-- =====================================================

-- 1. person table (base class for inheritance)
CREATE TABLE person (
    person_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20),
    address TEXT,
    role ENUM('STUDENT', 'LECTURER', 'ADMIN') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_person_email (email),
    INDEX idx_person_role (role)
);

-- 2. programme table (academic programs)
CREATE TABLE programme (
    programme_id INT AUTO_INCREMENT PRIMARY KEY,
    programme_code VARCHAR(20) UNIQUE NOT NULL,
    programme_name VARCHAR(100) NOT NULL,
    duration_years INT NOT NULL,
    description TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_programme_code (programme_code),
    INDEX idx_programme_active (is_active)
);

-- 3. department table
CREATE TABLE department (
    department_id INT AUTO_INCREMENT PRIMARY KEY,
    department_code VARCHAR(20) UNIQUE NOT NULL,
    department_name VARCHAR(100) NOT NULL,
    faculty VARCHAR(100),
    is_active BOOLEAN DEFAULT TRUE,
    INDEX idx_department_code (department_code)
);

-- 4. student table (extends person)
CREATE TABLE student (
    student_id INT PRIMARY KEY,
    registration_number VARCHAR(50) UNIQUE NOT NULL,
    programme_id INT NOT NULL,
    current_year INT DEFAULT 1,
    enrollment_date DATE NOT NULL,
    expected_graduation DATE,
    status ENUM('ACTIVE', 'GRADUATED', 'SUSPENDED', 'WITHDRAWN') DEFAULT 'ACTIVE',
    FOREIGN KEY (student_id) REFERENCES person(person_id) ON DELETE CASCADE,
    FOREIGN KEY (programme_id) REFERENCES programme(programme_id),
    INDEX idx_student_programme (programme_id),
    INDEX idx_student_reg_no (registration_number),
    INDEX idx_student_status (status)
);

-- 5. lecturer table (extends person)
CREATE TABLE lecturer (
    lecturer_id INT PRIMARY KEY,
    staff_number VARCHAR(50) UNIQUE NOT NULL,
    department_id INT,
    qualification VARCHAR(100),
    specialization VARCHAR(100),
    hire_date DATE NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (lecturer_id) REFERENCES person(person_id) ON DELETE CASCADE,
    FOREIGN KEY (department_id) REFERENCES department(department_id),
    INDEX idx_lecturer_staff (staff_number),
    INDEX idx_lecturer_department (department_id)
);

-- 6. admin table (extends person)
CREATE TABLE admin (
    admin_id INT PRIMARY KEY,
    employee_number VARCHAR(50) UNIQUE NOT NULL,
    role_description VARCHAR(100),
    FOREIGN KEY (admin_id) REFERENCES person(person_id) ON DELETE CASCADE,
    INDEX idx_admin_employee (employee_number)
);

-- =====================================================
-- SECTION 2: COURSE MANAGEMENT
-- =====================================================

-- 7. course table
CREATE TABLE course (
    course_id INT AUTO_INCREMENT PRIMARY KEY,
    course_code VARCHAR(20) UNIQUE NOT NULL,
    course_title VARCHAR(100) NOT NULL,
    credit_units INT NOT NULL,
    description TEXT,
    level ENUM('100', '200', '300', '400', '500') NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_course_code (course_code),
    INDEX idx_course_title (course_title),
    INDEX idx_course_level (level),
    FULLTEXT INDEX idx_course_search (course_title, course_code)
);

-- 8. programme_course (many-to-many with year/semester)
CREATE TABLE programme_course (
    programme_course_id INT AUTO_INCREMENT PRIMARY KEY,
    programme_id INT NOT NULL,
    course_id INT NOT NULL,
    year_level INT NOT NULL, -- 1,2,3,4
    semester ENUM('1', '2') NOT NULL,
    is_core BOOLEAN DEFAULT TRUE, -- TRUE for core, FALSE for elective
    is_active BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (programme_id) REFERENCES programme(programme_id),
    FOREIGN KEY (course_id) REFERENCES course(course_id),
    UNIQUE KEY unique_programme_course_year (programme_id, course_id, year_level, semester),
    INDEX idx_programme_course_programme (programme_id),
    INDEX idx_programme_course_course (course_id)
);

-- 9. academic_year table
CREATE TABLE academic_year (
    academic_year_id INT AUTO_INCREMENT PRIMARY KEY,
    year_code VARCHAR(20) UNIQUE NOT NULL, -- e.g., "2025/2026"
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    is_current BOOLEAN DEFAULT FALSE,
    INDEX idx_academic_year_current (is_current)
);

-- 10. semester table
CREATE TABLE semester (
    semester_id INT AUTO_INCREMENT PRIMARY KEY,
    academic_year_id INT NOT NULL,
    semester_number ENUM('1', '2', '3') NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    is_current BOOLEAN DEFAULT FALSE,
    registration_deadline DATE,
    FOREIGN KEY (academic_year_id) REFERENCES academic_year(academic_year_id),
    UNIQUE KEY unique_semester (academic_year_id, semester_number),
    INDEX idx_semester_current (is_current)
);

-- 11. course_offering (lecturer teaches course in a specific semester)
CREATE TABLE course_offering (
    offering_id INT AUTO_INCREMENT PRIMARY KEY,
    course_id INT NOT NULL,
    lecturer_id INT NOT NULL,
    semester_id INT NOT NULL,
    max_students INT DEFAULT 50,
    enrolled_count INT DEFAULT 0,
    status ENUM('PLANNED', 'ONGOING', 'COMPLETED', 'CANCELLED') DEFAULT 'PLANNED',
    FOREIGN KEY (course_id) REFERENCES course(course_id),
    FOREIGN KEY (lecturer_id) REFERENCES lecturer(lecturer_id),
    FOREIGN KEY (semester_id) REFERENCES semester(semester_id),
    UNIQUE KEY unique_offering (course_id, lecturer_id, semester_id),
    INDEX idx_offering_course (course_id),
    INDEX idx_offering_lecturer (lecturer_id),
    INDEX idx_offering_semester (semester_id),
    INDEX idx_offering_status (status)
);

-- 12. enrollment (student registers for course offering)
CREATE TABLE enrollment (
    enrollment_id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT NOT NULL,
    offering_id INT NOT NULL,
    enrollment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status ENUM('ENROLLED', 'DROPPED', 'COMPLETED') DEFAULT 'ENROLLED',
    FOREIGN KEY (student_id) REFERENCES student(student_id),
    FOREIGN KEY (offering_id) REFERENCES course_offering(offering_id),
    UNIQUE KEY unique_enrollment (student_id, offering_id),
    INDEX idx_enrollment_student (student_id),
    INDEX idx_enrollment_offering (offering_id),
    INDEX idx_enrollment_status (status)
);

-- =====================================================
-- SECTION 3: ACADEMIC GRADING
-- =====================================================

-- 13. score table (CAT and Exam)
CREATE TABLE score (
    score_id INT AUTO_INCREMENT PRIMARY KEY,
    enrollment_id INT UNIQUE NOT NULL,
    cat_1 DECIMAL(5,2) DEFAULT 0,
    cat_2 DECIMAL(5,2) DEFAULT 0,
    cat_3 DECIMAL(5,2) DEFAULT 0,
    exam DECIMAL(5,2) DEFAULT 0,
    total_cat DECIMAL(5,2) GENERATED ALWAYS AS (cat_1 + cat_2 + cat_3) STORED,
    total_score DECIMAL(5,2) GENERATED ALWAYS AS (cat_1 + cat_2 + cat_3 + exam) STORED,
    grade CHAR(2),
    remarks TEXT,
    graded_by INT,
    graded_date TIMESTAMP NULL,
    CHECK (cat_1 >= 0 AND cat_1 <= 30),
    CHECK (cat_2 >= 0 AND cat_2 <= 30),
    CHECK (cat_3 >= 0 AND cat_3 <= 30),
    CHECK (exam >= 0 AND exam <= 70),
    FOREIGN KEY (enrollment_id) REFERENCES enrollment(enrollment_id),
    FOREIGN KEY (graded_by) REFERENCES lecturer(lecturer_id),
    INDEX idx_score_enrollment (enrollment_id),
    INDEX idx_score_grade (grade)
);

-- 14. grade_scale table
CREATE TABLE grade_scale (
    grade_id INT AUTO_INCREMENT PRIMARY KEY,
    grade CHAR(2) UNIQUE NOT NULL,
    min_score INT NOT NULL,
    max_score INT NOT NULL,
    grade_point DECIMAL(3,2) NOT NULL,
    description VARCHAR(50),
    INDEX idx_grade_scale_range (min_score, max_score)
);

-- 15. result_slip table
CREATE TABLE result_slip (
    result_slip_id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT NOT NULL,
    semester_id INT NOT NULL,
    gpa DECIMAL(3,2),
    total_credits INT,
    status ENUM('DRAFT', 'PUBLISHED', 'ARCHIVED') DEFAULT 'DRAFT',
    generated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    published_date TIMESTAMP NULL,
    published_by INT,
    FOREIGN KEY (student_id) REFERENCES student(student_id),
    FOREIGN KEY (semester_id) REFERENCES semester(semester_id),
    FOREIGN KEY (published_by) REFERENCES admin(admin_id),
    UNIQUE KEY unique_result (student_id, semester_id),
    INDEX idx_result_student (student_id),
    INDEX idx_result_semester (semester_id),
    INDEX idx_result_status (status)
);

-- 16. result_detail table
CREATE TABLE result_detail (
    result_detail_id INT AUTO_INCREMENT PRIMARY KEY,
    result_slip_id INT NOT NULL,
    course_id INT NOT NULL,
    score DECIMAL(5,2),
    grade CHAR(2),
    credit_units INT,
    FOREIGN KEY (result_slip_id) REFERENCES result_slip(result_slip_id),
    FOREIGN KEY (course_id) REFERENCES course(course_id),
    UNIQUE KEY unique_result_detail (result_slip_id, course_id),
    INDEX idx_result_detail_slip (result_slip_id)
);

-- =====================================================
-- SECTION 4: LIBRARY MANAGEMENT
-- =====================================================

-- 17. book table (with all required fields)
CREATE TABLE book (
    book_id INT AUTO_INCREMENT PRIMARY KEY,
    isbn VARCHAR(20) UNIQUE,
    title VARCHAR(200) NOT NULL,
    author VARCHAR(100),
    publisher VARCHAR(100),
    edition VARCHAR(20),
    version VARCHAR(20),
    year_published YEAR,
    category VARCHAR(50),
    shelf_location VARCHAR(50),
    total_copies INT NOT NULL DEFAULT 1,
    available_copies INT NOT NULL DEFAULT 1,
    added_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CHECK (available_copies >= 0),
    CHECK (available_copies <= total_copies),
    INDEX idx_book_title (title),
    INDEX idx_book_isbn (isbn),
    INDEX idx_book_author (author),
    INDEX idx_book_category (category),
    FULLTEXT INDEX idx_book_search (title, author, publisher)
);

-- 18. borrow_record table
CREATE TABLE borrow_record (
    borrow_id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT NOT NULL,
    book_id INT NOT NULL,
    borrow_date DATE NOT NULL,
    due_date DATE NOT NULL,
    return_date DATE,
    fine_amount DECIMAL(10,2) DEFAULT 0,
    fine_paid BOOLEAN DEFAULT FALSE,
    status ENUM('BORROWED', 'RETURNED', 'OVERDUE', 'LOST') DEFAULT 'BORROWED',
    remarks TEXT,
    FOREIGN KEY (student_id) REFERENCES student(student_id),
    FOREIGN KEY (book_id) REFERENCES book(book_id),
    CHECK (due_date >= borrow_date),
    INDEX idx_borrow_student (student_id),
    INDEX idx_borrow_book (book_id),
    INDEX idx_borrow_dates (borrow_date, due_date),
    INDEX idx_borrow_status (status),
    INDEX idx_borrow_fine (fine_paid)
);

-- 19. reservation table
CREATE TABLE reservation (
    reservation_id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT NOT NULL,
    book_id INT NOT NULL,
    reservation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expiry_date DATE,
    status ENUM('PENDING', 'NOTIFIED', 'CANCELLED', 'COMPLETED') DEFAULT 'PENDING',
    notified_date TIMESTAMP NULL,
    queue_position INT,
    FOREIGN KEY (student_id) REFERENCES student(student_id),
    FOREIGN KEY (book_id) REFERENCES book(book_id),
    INDEX idx_reservation_student (student_id),
    INDEX idx_reservation_book (book_id),
    INDEX idx_reservation_status (status),
    INDEX idx_reservation_queue (queue_position)
);

-- =====================================================
-- SECTION 5: USER AUTHENTICATION & AUDIT
-- =====================================================

-- 20. user_account table (for login)
CREATE TABLE user_account (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    person_id INT UNIQUE NOT NULL,
    username VARCHAR(50) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    last_login TIMESTAMP NULL,
    login_attempts INT DEFAULT 0,
    is_locked BOOLEAN DEFAULT FALSE,
    password_changed_at TIMESTAMP NULL,
    FOREIGN KEY (person_id) REFERENCES person(person_id) ON DELETE CASCADE,
    INDEX idx_user_account_person (person_id),
    INDEX idx_user_account_username (username)
);

-- 21. audit_log table
CREATE TABLE audit_log (
    log_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    action_type ENUM('LOGIN', 'LOGOUT', 'CREATE', 'UPDATE', 'DELETE', 'BORROW', 'RETURN', 'RESERVE') NOT NULL,
    table_name VARCHAR(50),
    record_id INT,
    old_value TEXT,
    new_value TEXT,
    ip_address VARCHAR(45),
    user_agent TEXT,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user_account(user_id) ON DELETE SET NULL,
    INDEX idx_audit_user (user_id),
    INDEX idx_audit_timestamp (timestamp),
    INDEX idx_audit_action (action_type)
);

-- =====================================================
-- SECTION 6: INSERT DEFAULT DATA
-- =====================================================

-- Insert grade scale
INSERT INTO grade_scale (grade, min_score, max_score, grade_point, description) VALUES
('A', 70, 100, 4.00, 'Excellent'),
('B', 60, 69, 3.00, 'Good'),
('C', 50, 59, 2.00, 'Average'),
('D', 40, 49, 1.00, 'Pass'),
('F', 0, 39, 0.00, 'Fail');

-- Insert departments
INSERT INTO department (department_code, department_name, faculty) VALUES
('CS', 'Computer Science', 'Faculty of Computing'),
('IT', 'Information Technology', 'Faculty of Computing'),
('SE', 'Software Engineering', 'Faculty of Computing'),
('IS', 'Information Systems', 'Faculty of Computing');

-- Insert programmes
INSERT INTO programme (programme_code, programme_name, duration_years) VALUES
('BSC-CS', 'Bachelor of Science in Computer Science', 4),
('BSC-IT', 'Bachelor of Science in Information Technology', 4),
('BSC-SE', 'Bachelor of Science in Software Engineering', 4),
('MSC-CS', 'Master of Science in Computer Science', 2);

-- Insert sample courses
INSERT INTO course (course_code, course_title, credit_units, level) VALUES
('CSC301', 'Data Structures & Algorithms', 4, '300'),
('CSC305', 'Database Systems', 4, '300'),
('CSC310', 'Operating Systems', 3, '300'),
('CSC320', 'Computer Networks', 4, '300'),
('CSC330', 'Software Engineering', 4, '400');

-- Insert academic years
INSERT INTO academic_year (year_code, start_date, end_date, is_current) VALUES
('2024/2025', '2024-09-01', '2025-08-31', FALSE),
('2025/2026', '2025-09-01', '2026-08-31', TRUE),
('2026/2027', '2026-09-01', '2027-08-31', FALSE);

-- Insert semesters
INSERT INTO semester (academic_year_id, semester_number, start_date, end_date, is_current) VALUES
(2, '1', '2025-09-01', '2025-12-20', FALSE),
(2, '2', '2026-01-15', '2026-05-30', TRUE);

-- Insert sample books
INSERT INTO book (isbn, title, author, publisher, edition, version, year_published, category, total_copies, available_copies) VALUES
('978-0-13-110362-7', 'Introduction to Algorithms', 'Cormen et al.', 'MIT Press', '3rd', 'International', 2009, 'Programming', 10, 8),
('978-0-07-352332-3', 'Database System Concepts', 'Silberschatz', 'McGraw-Hill', '6th', NULL, 2010, 'Database', 8, 5),
('978-0-13-235088-4', 'Clean Code', 'Robert Martin', 'Prentice Hall', '1st', NULL, 2008, 'Programming', 5, 2),
('978-0-201-63361-0', 'Design Patterns', 'GoF', 'Addison-Wesley', '1st', NULL, 1994, 'Programming', 6, 4);

-- =====================================================
-- SECTION 7: CREATE TRIGGERS
-- =====================================================

DELIMITER $$

-- 1. Update available copies on borrow
CREATE TRIGGER after_borrow_insert
AFTER INSERT ON borrow_record
FOR EACH ROW
BEGIN
    IF NEW.status = 'BORROWED' THEN
        UPDATE book 
        SET available_copies = available_copies - 1 
        WHERE book_id = NEW.book_id;
    END IF;
END$$

-- 2. Update available copies on return
CREATE TRIGGER after_borrow_update_return
AFTER UPDATE ON borrow_record
FOR EACH ROW
BEGIN
    IF NEW.status = 'RETURNED' AND OLD.status != 'RETURNED' THEN
        UPDATE book 
        SET available_copies = available_copies + 1 
        WHERE book_id = NEW.book_id;
        
        -- Check if there are pending reservations
        UPDATE reservation 
        SET status = 'COMPLETED' 
        WHERE book_id = NEW.book_id AND student_id = NEW.student_id AND status = 'PENDING';
    END IF;
END$$

-- 3. Auto-calculate grade based on score
CREATE TRIGGER before_score_insert_calc_grade
BEFORE INSERT ON score
FOR EACH ROW
BEGIN
    DECLARE v_grade CHAR(2);
    
    IF NEW.total_score >= 70 THEN SET v_grade = 'A';
    ELSEIF NEW.total_score >= 60 THEN SET v_grade = 'B';
    ELSEIF NEW.total_score >= 50 THEN SET v_grade = 'C';
    ELSEIF NEW.total_score >= 40 THEN SET v_grade = 'D';
    ELSE SET v_grade = 'F';
    END IF;
    
    SET NEW.grade = v_grade;
    SET NEW.graded_date = NOW();
END$$

-- 4. Auto-calculate grade on update
CREATE TRIGGER before_score_update_calc_grade
BEFORE UPDATE ON score
FOR EACH ROW
BEGIN
    IF NEW.cat_1 != OLD.cat_1 OR NEW.cat_2 != OLD.cat_2 OR 
       NEW.cat_3 != OLD.cat_3 OR NEW.exam != OLD.exam THEN
       
        IF NEW.total_score >= 70 THEN SET NEW.grade = 'A';
        ELSEIF NEW.total_score >= 60 THEN SET NEW.grade = 'B';
        ELSEIF NEW.total_score >= 50 THEN SET NEW.grade = 'C';
        ELSEIF NEW.total_score >= 40 THEN SET NEW.grade = 'D';
        ELSE SET NEW.grade = 'F';
        END IF;
        
        SET NEW.graded_date = NOW();
    END IF;
END$$

-- 5. Check enrollment limit (max 5 courses per semester)
CREATE TRIGGER before_enrollment_insert_limit
BEFORE INSERT ON enrollment
FOR EACH ROW
BEGIN
    DECLARE enrollment_count INT;
    
    SELECT COUNT(*) INTO enrollment_count
    FROM enrollment e
    JOIN course_offering co ON e.offering_id = co.offering_id
    WHERE e.student_id = NEW.student_id
    AND co.semester_id = (SELECT semester_id FROM course_offering WHERE offering_id = NEW.offering_id)
    AND e.status = 'ENROLLED';
    
    IF enrollment_count >= 5 THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'Student cannot enroll in more than 5 courses per semester';
    END IF;
END$$

-- 6. Update enrolled_count in course_offering
CREATE TRIGGER after_enrollment_insert_count
AFTER INSERT ON enrollment
FOR EACH ROW
BEGIN
    IF NEW.status = 'ENROLLED' THEN
        UPDATE course_offering 
        SET enrolled_count = enrolled_count + 1 
        WHERE offering_id = NEW.offering_id;
    END IF;
END$$

-- 7. Update enrolled_count on enrollment drop
CREATE TRIGGER after_enrollment_update_count
AFTER UPDATE ON enrollment
FOR EACH ROW
BEGIN
    IF NEW.status = 'DROPPED' AND OLD.status = 'ENROLLED' THEN
        UPDATE course_offering 
        SET enrolled_count = enrolled_count - 1 
        WHERE offering_id = NEW.offering_id;
    END IF;
END$$

-- 8. Auto-create result slip when scores are complete
CREATE TRIGGER after_score_insert_update_results
AFTER INSERT ON score
FOR EACH ROW
BEGIN
    DECLARE v_student_id INT;
    DECLARE v_semester_id INT;
    DECLARE v_gpa DECIMAL(3,2);
    DECLARE v_total_credits INT;
    
    -- Get student and semester info
    SELECT e.student_id, co.semester_id 
    INTO v_student_id, v_semester_id
    FROM enrollment e
    JOIN course_offering co ON e.offering_id = co.offering_id
    WHERE e.enrollment_id = NEW.enrollment_id;
    
    -- Calculate GPA
    SELECT AVG(gs.grade_point), SUM(c.credit_units)
    INTO v_gpa, v_total_credits
    FROM score sc
    JOIN enrollment e ON sc.enrollment_id = e.enrollment_id
    JOIN course_offering co ON e.offering_id = co.offering_id
    JOIN course c ON co.course_id = c.course_id
    JOIN grade_scale gs ON sc.total_score BETWEEN gs.min_score AND gs.max_score
    WHERE e.student_id = v_student_id
    AND co.semester_id = v_semester_id;
    
    -- Insert or update result slip
    INSERT INTO result_slip (student_id, semester_id, gpa, total_credits, status)
    VALUES (v_student_id, v_semester_id, v_gpa, v_total_credits, 'DRAFT')
    ON DUPLICATE KEY UPDATE
        gpa = v_gpa,
        total_credits = v_total_credits,
        generated_date = CURRENT_TIMESTAMP;
END$$

-- 9. Audit log trigger
CREATE TRIGGER after_user_login_audit
AFTER UPDATE ON user_account
FOR EACH ROW
BEGIN
    IF NEW.last_login != OLD.last_login THEN
        INSERT INTO audit_log (user_id, action_type, timestamp)
        VALUES (NEW.user_id, 'LOGIN', NOW());
    END IF;
END$$

-- 10. Check overdue books on borrow
CREATE TRIGGER before_borrow_insert_check_overdue
BEFORE INSERT ON borrow_record
FOR EACH ROW
BEGIN
    IF NEW.due_date < CURDATE() AND NEW.status = 'BORROWED' THEN
        SET NEW.status = 'OVERDUE';
    END IF;
END$$

-- 11. Update reservation queue
CREATE TRIGGER after_book_return_update_reservations
AFTER UPDATE ON book
FOR EACH ROW
BEGIN
    IF NEW.available_copies > OLD.available_copies THEN
        -- Notify next in queue
        UPDATE reservation 
        SET status = 'NOTIFIED', notified_date = NOW()
        WHERE book_id = NEW.book_id 
        AND status = 'PENDING' 
        AND queue_position = 1;
    END IF;
END$$

DELIMITER ;

-- =====================================================
-- SECTION 8: CREATE EVENTS
-- =====================================================

-- Enable event scheduler
SET GLOBAL event_scheduler = ON;

-- Daily overdue check and fine calculation
DELIMITER $$
CREATE EVENT IF NOT EXISTS daily_overdue_check
ON SCHEDULE EVERY 1 DAY
STARTS TIMESTAMP(CURRENT_DATE, '00:00:00')
DO
BEGIN
    -- Update overdue status
    UPDATE borrow_record 
    SET status = 'OVERDUE',
        fine_amount = DATEDIFF(CURDATE(), due_date) * 10 -- KSh 10 per day
    WHERE due_date < CURDATE() 
    AND status = 'BORROWED';
    
    -- Log the action
    INSERT INTO audit_log (action_type, table_name, timestamp)
    VALUES ('UPDATE', 'borrow_record', NOW());
END$$

-- Weekly reservation expiry check
CREATE EVENT IF NOT EXISTS weekly_reservation_cleanup
ON SCHEDULE EVERY 1 WEEK
STARTS TIMESTAMP(CURRENT_DATE, '02:00:00')
DO
BEGIN
    UPDATE reservation 
    SET status = 'CANCELLED' 
    WHERE status = 'PENDING' 
    AND expiry_date < CURDATE();
END$$
DELIMITER ;

-- =====================================================
-- SECTION 9: CREATE VIEWS FOR REPORTING
-- =====================================================

-- 1. Student enrollment summary
CREATE VIEW vw_student_enrollment AS
SELECT 
    s.student_id,
    p.name AS student_name,
    s.registration_number,
    pr.programme_name,
    s.current_year,
    COUNT(e.enrollment_id) AS enrolled_courses,
    sem.semester_number,
    ay.year_code
FROM student s
JOIN person p ON s.student_id = p.person_id
JOIN programme pr ON s.programme_id = pr.programme_id
LEFT JOIN enrollment e ON s.student_id = e.student_id AND e.status = 'ENROLLED'
LEFT JOIN course_offering co ON e.offering_id = co.offering_id
LEFT JOIN semester sem ON co.semester_id = sem.semester_id
LEFT JOIN academic_year ay ON sem.academic_year_id = ay.academic_year_id
WHERE sem.is_current = 1 OR sem.is_current IS NULL
GROUP BY s.student_id, sem.semester_id;

-- 2. Current borrowed books with details
CREATE VIEW vw_current_borrowings AS
SELECT 
    br.borrow_id,
    p.name AS student_name,
    s.registration_number,
    b.title AS book_title,
    b.isbn,
    br.borrow_date,
    br.due_date,
    DATEDIFF(CURDATE(), br.due_date) AS days_overdue,
    br.fine_amount,
    br.status
FROM borrow_record br
JOIN student s ON br.student_id = s.student_id
JOIN person p ON s.student_id = p.person_id
JOIN book b ON br.book_id = b.book_id
WHERE br.status IN ('BORROWED', 'OVERDUE');

-- 3. Book availability summary
CREATE VIEW vw_book_availability AS
SELECT 
    book_id,
    title,
    author,
    isbn,
    total_copies,
    available_copies,
    (total_copies - available_copies) AS borrowed_copies,
    CASE 
        WHEN available_copies > 0 THEN 'Available'
        ELSE 'Not Available'
    END AS availability_status,
    shelf_location
FROM book;

-- 4. Student results summary
CREATE VIEW vw_student_results AS
SELECT 
    s.student_id,
    p.name AS student_name,
    s.registration_number,
    c.course_code,
    c.course_title,
    sc.cat_1,
    sc.cat_2,
    sc.cat_3,
    sc.exam,
    sc.total_score,
    sc.grade,
    sem.semester_number,
    ay.year_code,
    lp.name AS lecturer_name
FROM score sc
JOIN enrollment e ON sc.enrollment_id = e.enrollment_id
JOIN student s ON e.student_id = s.student_id
JOIN person p ON s.student_id = p.person_id
JOIN course_offering co ON e.offering_id = co.offering_id
JOIN course c ON co.course_id = c.course_id
JOIN semester sem ON co.semester_id = sem.semester_id
JOIN academic_year ay ON sem.academic_year_id = ay.academic_year_id
JOIN lecturer l ON co.lecturer_id = l.lecturer_id
JOIN person lp ON l.lecturer_id = lp.person_id;

-- 5. Lecturer teaching load
CREATE VIEW vw_lecturer_teaching_load AS
SELECT 
    l.lecturer_id,
    p.name AS lecturer_name,
    d.department_name,
    COUNT(DISTINCT co.offering_id) AS courses_taught,
    sem.semester_number,
    ay.year_code,
    SUM(co.enrolled_count) AS total_students
FROM lecturer l
JOIN person p ON l.lecturer_id = p.person_id
LEFT JOIN department d ON l.department_id = d.department_id
LEFT JOIN course_offering co ON l.lecturer_id = co.lecturer_id
LEFT JOIN semester sem ON co.semester_id = sem.semester_id
LEFT JOIN academic_year ay ON sem.academic_year_id = ay.academic_year_id
WHERE sem.is_current = 1 OR sem.is_current IS NULL
GROUP BY l.lecturer_id, sem.semester_id;

-- 6. Pending reservations
CREATE VIEW vw_pending_reservations AS
SELECT 
    r.reservation_id,
    p.name AS student_name,
    s.registration_number,
    b.title AS book_title,
    b.author,
    r.reservation_date,
    r.queue_position,
    r.status
FROM reservation r
JOIN student s ON r.student_id = s.student_id
JOIN person p ON s.student_id = p.person_id
JOIN book b ON r.book_id = b.book_id
WHERE r.status = 'PENDING'
ORDER BY r.queue_position;

-- =====================================================
-- SECTION 10: STORED PROCEDURES
-- =====================================================

DELIMITER $$

-- 1. Borrow book procedure
CREATE PROCEDURE sp_borrow_book(
    IN p_student_id INT,
    IN p_book_id INT,
    IN p_days INT
)
BEGIN
    DECLARE v_available INT;
    DECLARE v_already_borrowed INT;
    DECLARE v_result VARCHAR(255);
    
    -- Start transaction
    START TRANSACTION;
    
    -- Check availability
    SELECT available_copies INTO v_available 
    FROM book 
    WHERE book_id = p_book_id 
    FOR UPDATE;
    
    -- Check if already borrowed
    SELECT COUNT(*) INTO v_already_borrowed
    FROM borrow_record
    WHERE student_id = p_student_id 
    AND book_id = p_book_id 
    AND status IN ('BORROWED', 'OVERDUE');
    
    IF v_available > 0 AND v_already_borrowed = 0 THEN
        -- Create borrow record
        INSERT INTO borrow_record (student_id, book_id, borrow_date, due_date)
        VALUES (p_student_id, p_book_id, CURDATE(), DATE_ADD(CURDATE(), INTERVAL p_days DAY));
        
        -- Update book availability (trigger will handle this, but we'll do it explicitly)
        UPDATE book SET available_copies = available_copies - 1 WHERE book_id = p_book_id;
        
        -- Remove any pending reservation
        UPDATE reservation 
        SET status = 'COMPLETED' 
        WHERE student_id = p_student_id 
        AND book_id = p_book_id 
        AND status = 'PENDING';
        
        -- Audit log
        INSERT INTO audit_log (user_id, action_type, table_name, record_id)
        VALUES (p_student_id, 'BORROW', 'borrow_record', LAST_INSERT_ID());
        
        SET v_result = 'Book borrowed successfully';
        COMMIT;
        
        SELECT v_result AS message, 'success' AS status;
        
    ELSEIF v_already_borrowed > 0 THEN
        ROLLBACK;
        SELECT 'You already have this book borrowed' AS message, 'error' AS status;
    ELSE
        ROLLBACK;
        SELECT 'Book not available' AS message, 'error' AS status;
    END IF;
END$$

-- 2. Return book procedure
CREATE PROCEDURE sp_return_book(
    IN p_borrow_id INT
)
BEGIN
    DECLARE v_result VARCHAR(255);
    
    START TRANSACTION;
    
    UPDATE borrow_record 
    SET return_date = CURDATE(), 
        status = 'RETURNED',
        fine_amount = GREATEST(0, DATEDIFF(CURDATE(), due_date) * 10)
    WHERE borrow_id = p_borrow_id 
    AND status IN ('BORROWED', 'OVERDUE');
    
    IF ROW_COUNT() > 0 THEN
        -- Audit log
        INSERT INTO audit_log (user_id, action_type, table_name, record_id)
        VALUES ((SELECT student_id FROM borrow_record WHERE borrow_id = p_borrow_id), 
                'RETURN', 'borrow_record', p_borrow_id);
        
        SET v_result = 'Book returned successfully';
        COMMIT;
        
        SELECT v_result AS message, 'success' AS status;
    ELSE
        ROLLBACK;
        SELECT 'Invalid borrow record or book already returned' AS message, 'error' AS status;
    END IF;
END$$

-- 3. Reserve book procedure
CREATE PROCEDURE sp_reserve_book(
    IN p_student_id INT,
    IN p_book_id INT
)
BEGIN
    DECLARE v_available INT;
    DECLARE v_queue_position INT;
    DECLARE v_existing INT;
    
    START TRANSACTION;
    
    -- Check if already reserved
    SELECT COUNT(*) INTO v_existing
    FROM reservation
    WHERE student_id = p_student_id 
    AND book_id = p_book_id 
    AND status = 'PENDING';
    
    IF v_existing > 0 THEN
        ROLLBACK;
        SELECT 'You already have an active reservation for this book' AS message, 'error' AS status;
    ELSE
        -- Get queue position
        SELECT IFNULL(MAX(queue_position), 0) + 1 INTO v_queue_position
        FROM reservation
        WHERE book_id = p_book_id AND status = 'PENDING';
        
        -- Check availability
        SELECT available_copies INTO v_available FROM book WHERE book_id = p_book_id;
        
        IF v_available > 0 THEN
            -- Book is available, just borrow it
            ROLLBACK;
            CALL sp_borrow_book(p_student_id, p_book_id, 14);
        ELSE
            -- Add to reservation queue
            INSERT INTO reservation (student_id, book_id, queue_position, expiry_date)
            VALUES (p_student_id, p_book_id, v_queue_position, DATE_ADD(CURDATE(), INTERVAL 7 DAY));
            
            INSERT INTO audit_log (user_id, action_type, table_name, record_id)
            VALUES (p_student_id, 'RESERVE', 'reservation', LAST_INSERT_ID());
            
            COMMIT;
            SELECT 'Book reserved successfully' AS message, 'success' AS status, v_queue_position AS position;
        END IF;
    END IF;
END$$

-- 4. Search books procedure (with live search support)
CREATE PROCEDURE sp_search_books(
    IN p_search_term VARCHAR(100)
)
BEGIN
    SELECT 
        book_id,
        title,
        author,
        isbn,
        edition,
        year_published,
        available_copies,
        total_copies,
        CASE 
            WHEN available_copies > 0 THEN 'Available'
            ELSE 'Not Available'
        END AS status
    FROM book
    WHERE title LIKE CONCAT('%', p_search_term, '%')
       OR author LIKE CONCAT('%', p_search_term, '%')
       OR isbn LIKE CONCAT('%', p_search_term, '%')
    ORDER BY 
        CASE 
            WHEN title LIKE CONCAT(p_search_term, '%') THEN 1
            WHEN title LIKE CONCAT('%', p_search_term, '%') THEN 2
            ELSE 3
        END
    LIMIT 10;
END$$

-- 5. Get student result slip
CREATE PROCEDURE sp_get_student_results(
    IN p_student_id INT,
    IN p_semester_id INT
)
BEGIN
    -- Get student info
    SELECT 
        p.name AS student_name,
        s.registration_number,
        pr.programme_name,
        s.current_year,
        ay.year_code,
        sem.semester_number
    FROM student s
    JOIN person p ON s.student_id = p.person_id
    JOIN programme pr ON s.programme_id = pr.programme_id
    JOIN semester sem ON sem.semester_id = p_semester_id
    JOIN academic_year ay ON sem.academic_year_id = ay.academic_year_id
    WHERE s.student_id = p_student_id;
    
    -- Get course results
    SELECT 
        c.course_code,
        c.course_title,
        c.credit_units,
        sc.cat_1,
        sc.cat_2,
        sc.cat_3,
        sc.exam,
        sc.total_score,
        sc.grade,
        lp.name AS lecturer_name
    FROM enrollment e
    JOIN course_offering co ON e.offering_id = co.offering_id
    JOIN course c ON co.course_id = c.course_id
    LEFT JOIN score sc ON e.enrollment_id = sc.enrollment_id
    JOIN lecturer l ON co.lecturer_id = l.lecturer_id
    JOIN person lp ON l.lecturer_id = lp.person_id
    WHERE e.student_id = p_student_id
    AND co.semester_id = p_semester_id
    AND e.status = 'ENROLLED';
    
    -- Get summary
    SELECT 
        AVG(sc.total_score) AS average_score,
        (SELECT grade FROM grade_scale WHERE AVG(sc.total_score) BETWEEN min_score AND max_score) AS average_grade,
        SUM(c.credit_units) AS total_credits,
        AVG(gs.grade_point) AS gpa
    FROM enrollment e
    JOIN course_offering co ON e.offering_id = co.offering_id
    JOIN course c ON co.course_id = c.course_id
    LEFT JOIN score sc ON e.enrollment_id = sc.enrollment_id
    LEFT JOIN grade_scale gs ON sc.total_score BETWEEN gs.min_score AND gs.max_score
    WHERE e.student_id = p_student_id
    AND co.semester_id = p_semester_id
    AND e.status = 'ENROLLED';
END$$

-- 6. Get lecturer courses procedure
CREATE PROCEDURE sp_get_lecturer_courses(
    IN p_lecturer_id INT,
    IN p_semester_id INT
)
BEGIN
    SELECT 
        co.offering_id,
        c.course_code,
        c.course_title,
        c.credit_units,
        co.enrolled_count,
        co.max_students,
        pr.programme_name,
        sem.semester_number,
        ay.year_code,
        co.status
    FROM course_offering co
    JOIN course c ON co.course_id = c.course_id
    JOIN semester sem ON co.semester_id = sem.semester_id
    JOIN academic_year ay ON sem.academic_year_id = ay.academic_year_id
    LEFT JOIN programme_course pc ON c.course_id = pc.course_id
    LEFT JOIN programme pr ON pc.programme_id = pr.programme_id
    WHERE co.lecturer_id = p_lecturer_id
    AND (p_semester_id IS NULL OR co.semester_id = p_semester_id)
    ORDER BY c.course_code;
END$$

-- 7. Get students in course
CREATE PROCEDURE sp_get_course_students(
    IN p_offering_id INT
)
BEGIN
    SELECT 
        s.student_id,
        p.name AS student_name,
        s.registration_number,
        pr.programme_name,
        s.current_year,
        e.enrollment_date,
        sc.cat_1,
        sc.cat_2,
        sc.cat_3,
        sc.exam,
        sc.total_score,
        sc.grade
    FROM enrollment e
    JOIN student s ON e.student_id = s.student_id
    JOIN person p ON s.student_id = p.person_id
    JOIN programme pr ON s.programme_id = pr.programme_id
    LEFT JOIN score sc ON e.enrollment_id = sc.enrollment_id
    WHERE e.offering_id = p_offering_id
    AND e.status = 'ENROLLED'
    ORDER BY p.name;
END$$

-- 8. Submit scores procedure
CREATE PROCEDURE sp_submit_scores(
    IN p_lecturer_id INT,
    IN p_offering_id INT,
    IN p_student_scores JSON
)
BEGIN
    DECLARE v_enrollment_id INT;
    DECLARE v_cat1 DECIMAL(5,2);
    DECLARE v_cat2 DECIMAL(5,2);
    DECLARE v_cat3 DECIMAL(5,2);
    DECLARE v_exam DECIMAL(5,2);
    DECLARE v_done INT DEFAULT FALSE;
    DECLARE v_counter INT DEFAULT 0;
    DECLARE v_total INT;
    
    -- Verify lecturer teaches this course
    IF NOT EXISTS (SELECT 1 FROM course_offering 
                   WHERE offering_id = p_offering_id 
                   AND lecturer_id = p_lecturer_id) THEN
        SELECT 'You are not authorized to submit scores for this course' AS message, 'error' AS status;
    ELSE
        SET v_total = JSON_LENGTH(p_student_scores);
        
        WHILE v_counter < v_total DO
            SET v_enrollment_id = JSON_UNQUOTE(JSON_EXTRACT(p_student_scores, CONCAT('$[', v_counter, '].enrollment_id')));
            SET v_cat1 = JSON_UNQUOTE(JSON_EXTRACT(p_student_scores, CONCAT('$[', v_counter, '].cat_1')));
            SET v_cat2 = JSON_UNQUOTE(JSON_EXTRACT(p_student_scores, CONCAT('$[', v_counter, '].cat_2')));
            SET v_cat3 = JSON_UNQUOTE(JSON_EXTRACT(p_student_scores, CONCAT('$[', v_counter, '].cat_3')));
            SET v_exam = JSON_UNQUOTE(JSON_EXTRACT(p_student_scores, CONCAT('$[', v_counter, '].exam')));
            
            INSERT INTO score (enrollment_id, cat_1, cat_2, cat_3, exam, graded_by)
            VALUES (v_enrollment_id, v_cat1, v_cat2, v_cat3, v_exam, p_lecturer_id)
            ON DUPLICATE KEY UPDATE
                cat_1 = VALUES(cat_1),
                cat_2 = VALUES(cat_2),
                cat_3 = VALUES(cat_3),
                exam = VALUES(exam),
                graded_by = VALUES(graded_by),
                graded_date = NOW();
            
            SET v_counter = v_counter + 1;
        END WHILE;
        
        SELECT CONCAT('Successfully submitted ', v_total, ' scores') AS message, 'success' AS status;
    END IF;
END$$

-- 9. Dashboard statistics procedure
CREATE PROCEDURE sp_get_dashboard_stats()
BEGIN
    -- Student stats
    SELECT 
        (SELECT COUNT(*) FROM student WHERE status = 'ACTIVE') AS total_students,
        (SELECT COUNT(*) FROM student WHERE enrollment_date >= DATE_SUB(CURDATE(), INTERVAL 30 DAY)) AS new_students;
    
    -- Lecturer stats
    SELECT 
        COUNT(*) AS total_lecturers
    FROM lecturer WHERE is_active = TRUE;
    
    -- Book stats
    SELECT 
        COUNT(*) AS total_books,
        SUM(available_copies) AS available_books,
        SUM(total_copies - available_copies) AS borrowed_books,
        (SELECT COUNT(*) FROM borrow_record WHERE status = 'OVERDUE') AS overdue_books
    FROM book;
    
    -- Course stats
    SELECT 
        COUNT(*) AS total_courses,
        (SELECT COUNT(DISTINCT offering_id) FROM course_offering WHERE status = 'ONGOING') AS ongoing_courses;
    
    -- Recent borrowings
    SELECT 
        p.name AS student_name,
        b.title AS book_title,
        br.borrow_date,
        br.due_date,
        br.status
    FROM borrow_record br
    JOIN student s ON br.student_id = s.student_id
    JOIN person p ON s.student_id = p.person_id
    JOIN book b ON br.book_id = b.book_id
    ORDER BY br.borrow_date DESC
    LIMIT 5;
END$$

DELIMITER ;

-- =====================================================
-- SECTION 11: SAMPLE DATA FOR TESTING
-- =====================================================

-- Insert sample persons (for testing)
INSERT INTO person (name, email, phone, role) VALUES
('John Doe', 'john.doe@student.edu', '0712345678', 'STUDENT'),
('Dr. Sarah Johnson', 'sarah.johnson@edulib.com', '0723456789', 'LECTURER'),
('Mercy Wanjiku', 'mercy.wanjiku@student.edu', '0734567890', 'STUDENT'),
('Prof. Michael Chen', 'michael.chen@edulib.com', '0745678901', 'LECTURER'),
('Admin User', 'admin@edulib.com', '0756789012', 'ADMIN');

-- Insert students
INSERT INTO student (student_id, registration_number, programme_id, current_year, enrollment_date) VALUES
(1, 'STU001', 1, 3, '2023-09-01'),
(3, 'STU042', 3, 2, '2024-09-01');

-- Insert lecturers
INSERT INTO lecturer (lecturer_id, staff_number, department_id, hire_date) VALUES
(2, 'LEC001', 1, '2020-01-15'),
(4, 'LEC002', 2, '2019-03-10');

-- Insert admin
INSERT INTO admin (admin_id, employee_number) VALUES
(5, 'ADM001');

-- Insert user accounts
INSERT INTO user_account (person_id, username, password_hash) VALUES
(1, 'john.doe', '$2a$10$YourHashedPasswordHere'),
(2, 'sarah.johnson', '$2a$10$YourHashedPasswordHere'),
(3, 'mercy.wanjiku', '$2a$10$YourHashedPasswordHere'),
(4, 'michael.chen', '$2a$10$YourHashedPasswordHere'),
(5, 'admin', '$2a$10$YourHashedPasswordHere');

-- Insert course offerings
INSERT INTO course_offering (course_id, lecturer_id, semester_id, max_students) VALUES
(1, 2, 2, 50),
(2, 4, 2, 45),
(3, 2, 2, 40),
(4, 4, 2, 35);

-- Insert enrollments
INSERT INTO enrollment (student_id, offering_id) VALUES
(1, 1),
(1, 2),
(1, 3),
(3, 1),
(3, 2),
(3, 4);

-- Insert scores
INSERT INTO score (enrollment_id, cat_1, cat_2, cat_3, exam) VALUES
(1, 25, 26, 24, 58),
(2, 22, 23, 24, 62),
(3, 18, 19, 20, 45),
(4, 26, 25, 27, 65),
(5, 20, 21, 22, 48),
(6, 24, 25, 26, 55);

-- Insert sample borrow records
INSERT INTO borrow_record (student_id, book_id, borrow_date, due_date, status) VALUES
(1, 1, '2026-03-10', '2026-03-24', 'BORROWED'),
(1, 2, '2026-03-15', '2026-03-29', 'BORROWED'),
(3, 3, '2026-03-05', '2026-03-19', 'OVERDUE');

-- Insert sample reservations
INSERT INTO reservation (student_id, book_id, queue_position, expiry_date) VALUES
(3, 1, 1, DATE_ADD(CURDATE(), INTERVAL 7 DAY)),
(1, 4, 1, DATE_ADD(CURDATE(), INTERVAL 7 DAY));