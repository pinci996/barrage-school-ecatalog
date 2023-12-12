-- Add a changeset for seeding roles table
-- ChangeSet: seedRolesTable
-- Author: dbjelcevic
DO $$
BEGIN
  IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'roles') THEN
    INSERT INTO roles (role)
SELECT 'USER' WHERE NOT EXISTS (SELECT 1 FROM roles WHERE role = 'ROLE_USER');
END IF;
END $$;

-- Add a changeset for seeding users table
-- ChangeSet: seedUsersTable
-- Author: dbjelcevic
DO $$
BEGIN
  IF EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'users') THEN
    INSERT INTO users (username, password)
SELECT 'John', '$2a$12$h7hlBe0RjB4qumMcdk3AXuvTT/i/CEQDB2zGyR2E6AcQ6KvN6Uz02'
    WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'John');
END IF;
END $$;
