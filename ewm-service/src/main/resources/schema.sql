--DROP TABLE IF EXISTS request,users, categories, locations, events, requests,locations, compilations;

CREATE TABLE IF NOT EXISTS users (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL UNIQUE,
  email VARCHAR(255) NOT NULL,
  name VARCHAR(512) NOT NULL,
  CONSTRAINT pk_users PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS categories (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL UNIQUE,
  name VARCHAR(512) NOT NULL UNIQUE,
  CONSTRAINT pk_categories PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS locations (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL UNIQUE,
  lat FLOAT NOT NULL,
  lon FLOAT NOT NULL,
  CONSTRAINT pk_locations PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS compilations (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL UNIQUE,
  pinned BOOLEAN,
  title VARCHAR NOT NULL,
  CONSTRAINT pk_compilations PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS events (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL UNIQUE,
  annotation VARCHAR(255) NOT NULL,
  category_id BIGINT NOT NULL,
  confirmed_requests INTEGER,
  created_on TIMESTAMP,
  description VARCHAR(512),
  event_date TIMESTAMP NOT NULL,
  initiator_id BIGINT NOT NULL,
  location_id BIGINT NOT NULL,
  paid BOOLEAN NOT NULL,
  participant_limit INTEGER,
  published_on TIMESTAMP,
  request_moderation BOOLEAN,
  state VARCHAR,
  title VARCHAR NOT NULL,
  views INTEGER,
  compilations_id BIGINT,
  CONSTRAINT fk_events_to_compilations FOREIGN KEY (compilations_id) REFERENCES compilations (id),
  CONSTRAINT fk_events_to_users FOREIGN KEY (initiator_id) REFERENCES users (id),
  CONSTRAINT fk_events_to_categories FOREIGN KEY (category_id) REFERENCES categories (id),
  CONSTRAINT fk_events_to_locations FOREIGN KEY (location_id) REFERENCES locations (id),
  CONSTRAINT pk_events PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS requests (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL UNIQUE,
  created TIMESTAMP NOT NULL,
  event_id BIGINT NOT NULL,
  requester_id BIGINT NOT NULL,
  status VARCHAR NOT NULL,
  CONSTRAINT pk_requests PRIMARY KEY (id),
  CONSTRAINT fk_requests_to_users FOREIGN KEY (requester_id) REFERENCES users (id),
  CONSTRAINT fk_requests_to_events FOREIGN KEY (event_id) REFERENCES events (id)
);
