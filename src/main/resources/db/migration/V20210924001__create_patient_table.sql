CREATE TABLE patient (
  id UUID not null primary key,
  user_id text,
  first_name text not null,
  last_name text,
  email_address text,
  phone_number text,
  address text,
  created_date timestamp,
  updated_date timestamp
)