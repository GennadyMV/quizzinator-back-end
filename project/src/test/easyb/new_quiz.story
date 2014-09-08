description 'User can log in with valid username/password-combination'
	
	scenario "user can login with correct password", {
		given 'command login selected', {
			true
		}
		
		when 'a valid username and password are entered', {
			true
		}
		
		then 'user will be logged in to system', {
			true
		}
	}
	
	scenario "user can not login with incorrect password", {
		given 'command login selected'
		when 'a valid username and incorrect password are entered'
		then 'user will not be logged in to system'
	}
	
	scenario "nonexistent user can not login to ", {
		given 'command login selected'
		when 'a nonexistent username and some password are entered'
		then 'user will not be logged in to system'
	}