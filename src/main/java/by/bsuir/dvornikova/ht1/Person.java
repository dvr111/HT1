package by.bsuir.dvornikova.ht1;

import by.bsuir.dvornikova.ht1.util.DBWorker;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
@NoArgsConstructor
public class Person {

    // Данные записи о человеке.
    private String id = "0";
    private String name = "";
    private String surname = "";
    private String middleName = "";
    private HashMap<String, String> phones = new HashMap<>();

    // Конструктор для создания записи о человеке на основе данных из БД.
    public Person(String id, String name, String surname, String middleName) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.middleName = middleName;

        // Извлечение телефонов человека из БД.
        ResultSet db_data = DBWorker.getInstance().getDBData("SELECT * FROM `phone` WHERE `owner`=" + id);

        try {
            // Если у человека нет телефонов, ResultSet будет == null.
            if (db_data != null) {
                while (db_data.next()) {
                    this.phones.put(db_data.getString("id"), db_data.getString("number"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Конструктор для создания записи, предназначенной для добавления в БД.
    public Person(String name, String surname, String middleName) {
        this.name = name;
        this.surname = surname;
        this.middleName = middleName;
    }

    // Валидация частей ФИО. Для отчества можно передать второй параетр == true,
    // тогда допускается пустое значение.
    public boolean validateFMLNamePart(String fml_name_part, boolean empty_allowed) {
        if (empty_allowed) {
            Matcher matcher = Pattern.compile("[\\w-]{0,150}").matcher(fml_name_part);
            return matcher.matches();
        } else {
            Matcher matcher = Pattern.compile("[\\w-]{1,150}").matcher(fml_name_part);
            return matcher.matches();
        }

    }

}
