describe('example to-do app', () => {
    beforeEach(() => {
      // Cypress starts out with a blank slate for each test
      // so we must tell it to visit our website with the `cy.visit()` command.
      // Since we want to visit the same URL at the start of all our tests,
      // we include it in our beforeEach function so that it runs before each test
      
    })

    it('displays link text box and btton', () => {
        cy.visit('https://baby-ui-punitpatel214.cloud.okteto.net/#')
        // We use the `cy.get()` command to get all elements that match the selector.
        // Then, we use `should` to assert that there are two matched items,
        // which are the two default items.
        cy.get('#formurl').should('have.length', 1)
        cy.get('#formbutton').should('have.length', 1)
      })

      it('can generate baby url', () => {
        // We'll store our item text in a variable so we can reuse it
        const url = 'https://github.com/punitpatel214/baby-url-generator'
    
        cy.get('#formurl').type(`${url}`)
        cy.contains('Make Baby URL').click()
        cy.get('.boxtextleft', {timeout: 60000}).should('exist')
      })

      it('can naviagte back to create other', () => {
        cy.contains('baby URL').click()
        cy.get('#formurl').should('have.length', 1)
      })

      it('check redirection', () => {
        const url = 'https://github.com/punitpatel214/baby-url-generator'
    
        cy.get('#formurl').type(`${url}`)
        cy.contains('Make Baby URL').click()
        cy.get('.boxtextleft', {timeout: 60000}).should('exist')
        cy.get('input[type="text"]')
            .invoke('val')
            .then(url => {
                cy.request(url).its('body').should('include', 'punitpatel214')
            });
        
      })



    
})  