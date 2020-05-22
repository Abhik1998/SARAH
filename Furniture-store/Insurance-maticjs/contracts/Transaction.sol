// pragma solidity ^0.5.0;


// contract Transaction {
//     event PaymentReleased(address to, uint256 amount);
//     event PaymentReceived(address from, uint256 amount);

//     function () external payable {
//         emit PaymentReceived(msg.sender, msg.value);
//         uint amount = msg.value;
//         address payable pavitra = 0xF458852b007283c72a1D4A6F0bdE0b099e75faFb;
//         pavitra.transfer(9*amount/10);
//         address payable akshay = 0x068926E9ebD3eeB1879a3D3d7a54dF42260442ef;
//         akshay.transfer(amount/10);
//         emit PaymentReleased(pavitra, 9*amount/10);
//         emit PaymentReleased(akshay, amount/10);
//     }

//     function release(uint amount) public payable {
//       address payable pavitra = 0x068926E9ebD3eeB1879a3D3d7a54dF42260442ef;
//       pavitra.transfer(9*amount/10);
//       address payable akshay = 0xF458852b007283c72a1D4A6F0bdE0b099e75faFb;
//       akshay.transfer(amount/10);
//       emit PaymentReleased(pavitra, 9*amount/10);
//       emit PaymentReleased(akshay, amount/10);
//     }
// }


pragma solidity ^0.5.0;

contract Transaction {

    event PaymentReleased(address to, uint256 amount);
    event PaymentReceived(address from, uint256 amount);

    mapping(address => uint256) private _shares;
    uint256 private _totalShares;
    address payable[] private _payees;

    function () external payable {
        emit PaymentReceived(msg.sender, msg.value);
        uint amount = msg.value;
        _payees.push(0x27A84e17bEdDFb4cEc1b7De36f4765216A5FD19a);
        _shares[0x27A84e17bEdDFb4cEc1b7De36f4765216A5FD19a] = 9;
        _totalShares += 9;
        _payees.push(0xF458852b007283c72a1D4A6F0bdE0b099e75faFb);
        _shares[0xF458852b007283c72a1D4A6F0bdE0b099e75faFb] = 1;
        _totalShares += 1;
        for (uint i = 0; i < 2; i++){
            _payees[i].transfer(amount*(_shares[_payees[i]]/_totalShares));
            emit PaymentReleased(_payees[i], amount*(_shares[_payees[i]]/_totalShares));
        }
    }

    /**
     * @dev Getter for the total shares held by payees.
     */
    function totalShares() public view returns (uint256) {
        return _totalShares;
    }

    /**
     * @dev Getter for the amount of shares held by an account.
     */
    function shares(address account) public view returns (uint256) {
        return _shares[account];
    }

    /**
     * @dev Getter for the address of the payee number `index`.
     */
    function payee(uint256 index) public view returns (address) {
        return _payees[index];
    }
}