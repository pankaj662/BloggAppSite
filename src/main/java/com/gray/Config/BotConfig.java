package com.gray.Config;

import java.util.Set;

import org.springframework.stereotype.Component;

@Component
public class BotConfig {
	public final Set<String> BLOKED_USER_AGENTS = Set.of(

			"paython-request", 
			"scrapy",
		//	"postmanruntime", 
			"curl"
			);

	public final Set<String> BLOCKED_HEADER = Set.of(

			"X-Api-Key", 
			"X-Scraper", 
			"X-Requested-With"
			);
	
	public final Set<String> API_BLOCKED_ADMIN=Set.of(
			"/admin",
			"/admin/login",
			"/admin/home",
			"/admin/index",
			"/admin-panel",
			"/admin-console",
			"/admin/admin",
			"/administrator",
			"/wp-admin",
			"/cpanel",
			"/login",
			"/login/admin",
			"/manager/html",
			"/dashboard",
			"/backend",
			"/admin123",
			"/secret-admin",	
			"/api/admin",
			"/api/v1/admin",
			"/admin/verify",
			"/admin/validate",
			"/admin/secret",
			"/token/generate",
			"/reset-password",
			"/api/verify",
			"/api/v1/admin/backup",
			"/api/v1/admin/deleteAll",
			"/api/v1/admin/logs",
			"/api/v1/internal",
			"/api/v1/secret",
			"/.hidden",
			"/admin/secret-login"
			);
	
	public final Set<String> API_BLOCKED_USER=Set.of(
			"/user/login",
			"/api/login",
			"/api/user/all",
			"/api/users",
			"/api/v1/delete-all",
			"/api/v1/backup",
			"/api/v1/get-config",
			"/api/settings",
			"/api/authenticate",
			"/api/auth/login",
			"/api/register",
			"/api/v1/root",
			"/api/root/access",
			"/api/token",
			"/api/v1/user/",
			"/api/v1/maneger",
			"/api/v1/admin",
			"/api/v1/user",
			"/api/v1/users",
			"/api/v1/auth/login",
			"/api/v1/auth/signup",
			"/api/v1/auth/refresh",
			"/api/v1/getAll",
			"/api/v1/delete",
			"/api/v1/create",
			"/api/v1/upload",
			"/api/v1/profile"

			
			);
	
	public final Set<String> EXTRAAPI=Set.of(
			"/api/v1/internal/db-dump",
			"/api/v1/internal/config",
			"/api/v1/internal/keys",
			"/api/v1/internal/system-info",
			"/api/v1/internal/backup-files",
			"/api/v1/user-data/get-all-users",
			"/api/v1/user-data/passwords",
			"/api/v1/user-data/export",
			"/api/v1/user-data/internal-report",
			"/api/v1/user-data/secret",
			"/system/root",
			"/system/env",
			"/system/config",
			"/system/restart",
			"/system/logs",

			"/.env",
			"/.git",
			"/.htaccess",
			"/.htpasswd",
			"/server-status",
			"/phpinfo",
			"/info.php",
			"/config",
			"/config.json",
			"/config.php",
			"/.bash_history",
			"/.dockerignore",
			"/docker-compose.yml",
			"/package.json",
			"/yarn.lock",
			"/wp-login.php",
			"/wp-config.php",
			"/wp-admin/install.php",
			"/xmlrpc.php",
			"/wp-json/wp/v2/users",
			"/wp-admin/setup-config.php",
			"/products?id=1",
			"/items?id=1",
			"/search?q=test",
			"/login?user=admin",
			"/api/user?id=1",
			"/upload",
			"/file/upload",
			"/image/upload",
			"/api/v1/upload",
			"/admin/upload",
			"/uploads",
			"/media/upload",
			"/backup.zip",
			"/db.sql",
			"/database.sql",
			"/site.zip",
			"/backup.tar.gz",
			"/config.old",
			"/index.php.bak",
			"/storage/logs/laravel.log",
			"/server.js",
			"/app.js",
			"/actuator/env",
			"/actuator/mappings",
			"/metadata/v1",
			"/latest/meta-data",
			"/documents/upload",
			"/uploadfile",
			"/upload.php",
			"/actuator",
			"/actuator/health",
			"/actuator/info",
			"/swagger-ui",
			"/swagger",
			"/v3/api-docs",
			"/h2-console",
			"/test",
			"/tmp",
			"/logs",
			"/private",
			"/secret",
			"/internal",
			"/debug",
			"/graphql",
			"/api/graphql",
			"/search",
			"/product?id=1",
			"/users?id=1",
			"/?id=1",
			"/?page=1",
			"/wp-admin",
			"/wp-content",
			"/wp-json"






			);
}
