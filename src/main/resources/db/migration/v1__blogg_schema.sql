CREATE TABLE 
 Users(
	id SERIAL PRIMARY KEY,
	user_name VARCHAR(255) NOT NULL,
	user_email VARCHAR(255) NOT NULL UNIQUE,
	user_password VARCHAR(255) NOT NULL,
	user_about VARCHAR(500) NOT NULL,
	
	profile_image VARCHAR(255) NOT NULL,
	
    created_at TIMESTAMP NOT NULL,
    update_at TIMESTAMP NOT NULL,
    Last_Login TIMESTAMP ,
    Created_By VARCHAR(255) NOT NULL,
    Upadte_By VARCHAR(255) NOT NULL,
    User_Profile BOOLEAN NOT NULL DEFAULT TRUE,
    Temporary_Block TIMESTAMP ,
    Parmanent_block BOOLEAN NOT NULL DEFAULT FALSE,
    JwtToken_Version DOUBLE NOT NULL DEFAULT 1.09,
    roles VARCHAR(50) NOT NULL 
    
 );
 
 CREATE TABLE
Categories(
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL UNIQUE,
    description VARCHAR(1000) NOT NULL   	
);
 
CREATE TABLE
Posts(
	postId SERIAL PRIMARY KEY,
	Post_Title VARCHAR(255) NOT NULL,
	Post_Content TEXT NOT NULL,
	PostImage VARCHAR(255) NOT NULL,
	Date TIMESTAMP NOT NULL,
	CategorieId INT NOT NULL,
	UserId INT NOT NULL,
	
	
	CONSTRAINT fk_posts_categoreis
	FOREIGN KEY (CategorieId)
	REFERENCES Categories(id)
	ON DELETE CASCADE,
	
	CONSTRAINT fk_posts_user
	FOREIGN KEY (UserId)
	REFERENCES Users(id)
	ON DELETE CASCADE
	
);

CREATE TABLE
comment(
	id SERIAL PRIMARY KEY,
	comment_content VARCHAR(1000) NOT NULL,
	post_id INT NOT NULL,
	user_id INT NOT NULL,
	
	CONSTRAINT fk_comment_postId
	FOREIGN KEY (post_id)
	REFERENCES Posts(postId)
	ON DELETE CASCADE,
	
	CONSTRAINT fk_comment_userId
	FOREIGN KEY (user_id)
	REFERENCES Users(id)
	ON DELETE CASCADE	
);


CREATE TABLE
Delay(
    id SERIAL PRIMARY KEY,
    userId INT ,
    attempCount INT,
    timeManege INT,
    delay TIMESTAMP   
);



CREATE TABLE
ip (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    ip_address VARCHAR(45) NOT NULL,
    request_reach_count INTEGER DEFAULT 0,
    block_start_time TIMESTAMP,
    temp_block_count INTEGER DEFAULT 0,
    total_temp_block INTEGER DEFAULT 0,
    permanently_blocked BOOLEAN DEFAULT FALSE,
    last_temp_blocked TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_user_ip UNIQUE (user_id, ip_address)
);

-- ===============================
-- Indexes (Performance)
-- ===============================

CREATE INDEX  idx_ip_user_id
ON ip (user_id);
CREATE INDEX  idx_ip_address
ON ip (ip_address);
CREATE INDEX  idx_ip_blocked
ON ip (permanently_blocked);

-- ===============================
-- Trigger: Prevent update of immutable columns
-- (Equivalent of updatable = false)
-- ===============================

CREATE OR REPLACE FUNCTION prevent_immutable_ip_update()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.user_id <> OLD.user_id THEN
        RAISE EXCEPTION 'user_id cannot be updated';
    END IF;

    IF NEW.ip_address <> OLD.ip_address THEN
        RAISE EXCEPTION 'ip_address cannot be updated';
    END IF;

    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_prevent_ip_update
BEFORE UPDATE ON ip
FOR EACH ROW
EXECUTE FUNCTION prevent_immutable_ip_update();


-- ======================================
-- Table: refreshtoken
-- Purpose: Refresh Token Rotation
-- Database: PostgreSQL
-- ======================================

CREATE TABLE IF NOT EXISTS refreshtoken (
    id BIGSERIAL PRIMARY KEY,

    user_id BIGINT NOT NULL,

    token VARCHAR(500) NOT NULL UNIQUE,

    valid BOOLEAN NOT NULL DEFAULT TRUE,

    expiry TIMESTAMP NOT NULL,

    revoked BOOLEAN DEFAULT FALSE,

    used BOOLEAN DEFAULT FALSE,

    user_agent VARCHAR(255),

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ======================================
-- Indexes (Performance & Security)
-- ======================================

CREATE INDEX IF NOT EXISTS idx_refresh_token_user_id
ON refreshtoken (user_id);

CREATE INDEX IF NOT EXISTS idx_refresh_token_valid
ON refreshtoken (valid);

CREATE INDEX IF NOT EXISTS idx_refresh_token_expiry
ON refreshtoken (expiry);
