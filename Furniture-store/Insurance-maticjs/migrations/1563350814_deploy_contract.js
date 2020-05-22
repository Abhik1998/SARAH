var TransactionContract = artifacts.require('Transaction');

module.exports = function(deployer) {
  // Use deployer to state migration tasks.
  deployer.deploy(TransactionContract);

};
