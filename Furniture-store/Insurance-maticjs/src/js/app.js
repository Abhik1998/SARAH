// App = {
//     web3Provider: null,
//     contracts: {},
  
//     init: async function() {
//       return await App.initWeb3();
//     },
  
//     initWeb3: async function() {
//       if (window.ethereum) {
//         App.web3Provider = window.ethereum;
//         try {
//           // Request account access
//           await window.ethereum.enable();
//         } catch (error) {
//           // User denied account access...
//           console.error("User denied account access")
//         }
//       }
//       // Legacy dapp browsers...
//       else if (window.web3) {
//         App.web3Provider = window.web3.currentProvider;
//       }
//       // If no injected web3 instance is detected, fall back to Ganache
//       else {
//         App.web3Provider = new Web3.providers.HttpProvider('http://localhost:7545');
//       }
//       web3 = new Web3(App.web3Provider);
  
//       return App.initContract();
//     },
  
//     initContract: function() {
//       $.getJSON('Transaction.json', function(data) {
//         // Get the necessary contract artifact file and instantiate it with truffle-contract
//         var TransactionArtifact = data;
//         App.contracts.Transaction = TruffleContract(TransactionArtifact);
      
//         // Set the provider for our contract
//         App.contracts.Transaction.setProvider(App.web3Provider);
//       });
  
//       return App.bindEvents();
//     },
  
//     bindEvents: function() {
//       $(document).on('click', '.btn-transact', App.buyPolicy);
//     },
  
//     buyPolicy: function(event) {
//       var transactionInstance;
//       var contractAddress = "0xa111784937a546b7960bC85aBBC26F742e30a2E6"; // for rinkeby
//       let amount = 2000000000000000000;  
//       web3.eth.getAccounts(function(error, accounts) {
//         if (error) {
//           console.log(error);
//         }
//         var account = accounts[0];
//         App.contracts.Transaction.deployed().then(function(instance) {
//           transactionInstance = instance;
//           web3.eth.sendTransaction({from:account,to:transactionInstance.address, value:amount, gas:100000}, function(error, result){
//             if (error) {
//                 console.log(error);
//                 alert("Please accept Metamask request to buy policy.")
//               }
//               else{
//                 // transactionInstance.release(amount);
//               }
//           }); 
//         }).catch(function(err) {
//           console.log(err.message);
//           alert("Something went wrong. Please try again.");
//         });
//       });
//     }
    
//   };

//     $(document).ready(function(){
//       App.init();
//   });
  


App = {
    web3Provider: null,
    contracts: {},
  
    init: async function() {
      return await App.initWeb3();
    },
  
    initWeb3: async function() {
      if (window.ethereum) {
        App.web3Provider = window.ethereum;
        try {
          // Request account access
          await window.ethereum.enable();
        } catch (error) {
          // User denied account access...
          console.error("User denied account access")
        }
      }
      // Legacy dapp browsers...
      else if (window.web3) {
        App.web3Provider = window.web3.currentProvider;
      }
      // If no injected web3 instance is detected, fall back to Ganache
      else {
        App.web3Provider = new Web3.providers.HttpProvider('http://localhost:7545');
      }
      web3 = new Web3(App.web3Provider);
  
      return App.initContract();
    },
  
    initContract: function() {
      $.getJSON('Transaction.json', function(data) {
        // Get the necessary contract artifact file and instantiate it with truffle-contract
        var TransactionArtifact = data;
        App.contracts.Transaction = TruffleContract(TransactionArtifact);
      
        // Set the provider for our contract
        App.contracts.Transaction.setProvider(App.web3Provider);
      });
  
      return App.bindEvents();
    },
  
    bindEvents: function() {
      $(document).on('click', '.btn-transact', App.buyPolicy);
    },
  
    buyPolicy: function(event) {
      var transactionInstance;
      var amount = (parseInt(event.target.id))*1000000000000000000;
      web3.eth.getAccounts(function(error, accounts) {
        if (error) {
          console.log(error);
        }
        var account = accounts[0];
        var balance = web3.eth.getBalance(web3.eth.coinbase, function(error, result){
          if(error){
            console.log(error.message);
          }
        });
        console.log(balance);
        if (balance > amount) {
          App.contracts.Transaction.deployed().then(function(instance) {
            transactionInstance = instance;
            web3.eth.sendTransaction({from:account, to:transactionInstance.address, value:amount, gas:100000}, function(error, result){
              if (error) {
                  console.log(error);
                  alert("Please accept Metamask request to buy policy.")
              }
            }); 
          }).catch(function(err) {
            console.log(err.message);
            alert("Something went wrong. Please try again.");
          });
        } else alert("Insufficient balance.");
      });
    }
};
$(function() {
    $(window).load(function() {
      App.init();
    });
});
  