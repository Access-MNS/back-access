package com.alert.alert.faker;

import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class FakerGenerator {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FakerGenerator(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void generateFake(int count) {
        Faker faker = new Faker();

        for (int i = 1; i <= count; i++) {
            insertFakeUsers(faker);
            insertFakeChannels(faker, i);
            insertFakeMessages(faker, i);
            insertFakeChannelUser_GUs(faker, i);
        }

        System.out.println("Fake datas inserted successfully!");
    }

    private void insertFakeUsers(Faker faker) {
        String password = faker.internet().password();
        String lastName = faker.name().lastName();
        String firstName = faker.name().firstName();
        String pseudo = faker.name().username();
        String role = faker.options().option("USER", "ADMIN");

        String insertQuery = "INSERT INTO users (password, mail, role, last_name, first_name, " +
                "created_by, created_date, modified_by, modified_date, last_seen) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(insertQuery, password, pseudo, role, lastName, firstName,
                pseudo, new Date(), pseudo, new Date(), new Date());
    }

    private void insertFakeChannels(Faker faker, int i) {
        String name = faker.lorem().word();
        String creator = faker.name().username();
        String description = faker.lorem().sentence();
        int parent = faker.number().numberBetween(1, i);

        String insertQuery = "INSERT INTO channels (name, description, parent_channel_id, " +
                "created_by, created_date, modified_by, modified_date) " +
                "VALUES (?,?,?,?,?,?,?)";

        jdbcTemplate.update(insertQuery, name, description, parent, creator, new Date(), creator, new Date());
    }

    //One group many users
    private void insertFakeChannelUser_GUs(Faker faker, int i) {
        int channel = faker.number().numberBetween(1, i);
        boolean canEdit = faker.bool().bool();
        boolean canDelete = faker.bool().bool();
        boolean canInvite = faker.bool().bool();
        boolean canView = faker.bool().bool();
        String creator = faker.name().username();

        String insertQuery = "INSERT INTO channels_users (channel_id, user_id, can_edit, can_delete, " +
                "can_invite, can_view, created_by, created_date, modified_by, modified_date) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?)";

        jdbcTemplate.update(insertQuery, channel, i, canEdit, canDelete, canInvite, canView,
                creator, new Date(), creator, new Date());
    }

    //One user many groups
    private void insertFakeChannelUser_GsU(Faker faker, int i) {
        int user = faker.number().numberBetween(1, i);
        boolean canEdit = faker.bool().bool();
        boolean canDelete = faker.bool().bool();
        boolean canInvite = faker.bool().bool();
        boolean canView = faker.bool().bool();
        String creator = faker.name().username();

        String insertQuery = "INSERT INTO channels_users (channel_id, user_id, can_edit, can_delete, " +
                "can_invite, can_view, created_by, created_date, modified_by, modified_date) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?)";

        jdbcTemplate.update(insertQuery, i, user, canEdit, canDelete, canInvite, canView,
                creator, new Date(), creator, new Date());
    }

    private void insertFakeMessages(Faker faker, int i) {
        int sender = faker.number().numberBetween(1, i);
        String action = faker.options().option("JOINED", "COMMENTED", "LEFT");
        String comment = faker.lorem().sentence();
        boolean isDeleted = faker.bool().bool();

        String insertQuery = "INSERT INTO messages (sender, action, comment, is_deleted, " +
                "created_by, created_date, modified_by, modified_date) " +
                "VALUES (?,?,?,?,?,?,?,?)";

        jdbcTemplate.update(insertQuery, sender, action, comment, isDeleted, sender, new Date(), sender, new Date());
    }
}
