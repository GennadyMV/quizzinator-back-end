module.exports = {
  	"Adding quiz item adds item on the list" : function (browser) {
    	browser
	      	.url('localhost:8080/#/quiz/new')
		    .waitForElementVisible('body', 1000)
		    .setValue('#new-quiz-title','Kysely')
		    .waitForElementVisible('#add-item', 1000)
		    .click('#add-item')
		    .waitForElementVisible('#add-open-question', 1000)
		    .click('#add-open-question')
		    .waitForElementVisible('#new-open-question-question', 1000)
		    .setValue('#new-open-question-question', 'Perustele')
		    .waitForElementVisible('#create-open-question', 1000)
		    .click('#create-open-question')
		    .pause(1000)
		    .assert.containsText('#quiz-item-list', 'Perustele')
		    .end();
  	},
 	"Adding quiz saves it in the database" : function(browser){
  		browser
  			.url('localhost:8080/#/quiz/new')
  			.waitForElementVisible('body', 1000)
  			.setValue('#new-quiz-title','Kysely')
  			.click('#save-quiz')
  			.waitForElementVisible('.alert-success', 1000)
  			.assert.containsText('.alert-success', 'The quiz has been saved!')
  			.click('.navbar-nav li:first-child a')
  			.waitForElementVisible('.table', 1000)
  			.pause(1000)
  			.assert.containsText('.table', 'Kysely')
  	}
};